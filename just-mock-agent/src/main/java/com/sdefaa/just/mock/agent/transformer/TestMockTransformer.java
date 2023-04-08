package com.sdefaa.just.mock.agent.transformer;

import com.sdefaa.just.mock.agent.config.JustMockAgentConfigLoader;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;
import com.sdefaa.just.mock.agent.core.pojo.TargetMethod;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import sun.misc.Launcher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author Julius Wong
 * @since 1.0.o0
 */
public class TestMockTransformer {

  private static final Logger logger = Logger.getLogger(TestMockTransformer.class.getName());



  public byte[] transform(TargetClass targetClass,List<String> environmentVariableList) throws IllegalClassFormatException {
    CtClass ctClass = null;
    try {
      String targetClassName = targetClass.getClazz().getName();

      ClassPool pool = new ClassPool();
      System.out.println(targetClass.getClazz().getClassLoader());


      pool.insertClassPath(new LoaderClassPath(targetClass.getClazz().getClassLoader()));
     // pool.insertClassPath(new LoaderClassPath(Launcher.getLauncher().getClassLoader()));
      try {
        ctClass= pool.get(targetClass.getClazz().getName());
        if (ctClass.isFrozen()){
          System.out.println("defrost"+targetClassName);
          ctClass.defrost();
        }
      }catch (Exception e){
        InputStream inputStream = targetClass.getClazz().getResourceAsStream("/"+targetClassName.replace(".","/")+".class");
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        pool.makeClass(new ByteArrayInputStream(bytes));
        ctClass = pool.makeClass(inputStream);
      }



//      ctClass = pool.makeClass(new ByteArrayInputStream(ctClass1.toBytecode()));
      for (TargetMethod targetMethod : targetClass.getTargetMethods()) {
        CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethod.getMethodName());
        logger.info(ctMethod.getReturnType().getName());
        boolean isVoidReturnType = Objects.equals(ctMethod.getReturnType().getName(), "void");
        CtClass[] parameterTypes = ctMethod.getParameterTypes();
        StringBuilder parameters = new StringBuilder();
        if (parameterTypes.length > 0) {
          parameters.append(",new Object[]{");
          if (Objects.isNull(environmentVariableList) || environmentVariableList.isEmpty()) {
            for (int i = 1; i < parameterTypes.length; i++) {
              parameters.append("(Object)$" + i + ",");
            }
            parameters.append("(Object)$" + parameterTypes.length);
          } else {
            for (int i = 1; i < parameterTypes.length + 1; i++) {
              parameters.append("(Object)$" + i + ",");
            }
            for (int i = 0; i < environmentVariableList.size() - 1; i++) {
              parameters.append("(Object)" + environmentVariableList.get(i) + ",");
            }
            parameters.append("(Object)" + environmentVariableList.get(environmentVariableList.size() - 1));
          }
          parameters.append("}");
        } else {
          parameters.append(",null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("try{");
        sb.append("if(com.sdefaa.just.mock.common.strategy.MockManager.INSTANCE.shouldMock(\"" + targetClassName + "\",\"" + targetMethod.getMethodName() + "\"");
        sb.append(parameters);
        sb.append(")){");
        if (isVoidReturnType) {
          sb.append("com.sdefaa.just.mock.common.strategy.MockManager.INSTANCE.doMock(\"" + targetClassName + "\",\"" + targetMethod.getMethodName() + "\",$type");
        } else {
          sb.append("return (" + ctMethod.getReturnType().getName() + ")com.sdefaa.just.mock.common.strategy.MockManager.INSTANCE.doMock(\"" + targetClassName + "\",\"" + targetMethod.getMethodName() + "\",$type");
        }
        sb.append(parameters);
        sb.append(");");
        sb.append("}");
        sb.append("}catch(Throwable e){}");
        ctMethod.insertBefore(sb.toString());
      }
      byte[] byteCode = ctClass.toBytecode();
      ctClass.detach();
      Boolean debug = JustMockAgentConfigLoader.INSTANCE.agentConfigProperties().getDebug();
      if (debug) {
        Path byteCodePath = Paths.get(targetClassName + ".class");
        Files.write(byteCodePath, byteCode);
      }
      return byteCode;
    } catch (Throwable e) {
      logger.info(e.toString());
      // MockAgentMain.TRANSFORM_HAS_EXCEPTION.compareAndSet(false, true);
      logger.info("目标类的方法修改异常," + e);
      throw new IllegalClassFormatException("目标类的方法修改异常" + e);
    }
  }
}
