package com.sdefaa.just.mock.agent.transformer;

import com.sdefaa.just.mock.agent.MockAgentMain;
import com.sdefaa.just.mock.agent.config.JustMockAgentConfigLoader;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
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
 * @since 1.0.0
 */
public class MockClassFileTransformer implements ClassFileTransformer {
  private static final Logger logger = Logger.getLogger(MockClassFileTransformer.class.getName());
  private final String className;
  private final String methodName;
  private final List<String> environmentVariableList;

  public MockClassFileTransformer(String className, String methodName, List<String> environmentVariableList) {
    this.className = className;
    this.methodName = methodName;
    this.environmentVariableList = environmentVariableList;
  }

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    if (className.equals(this.className.replace('.', '/'))) {
      try {
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
        CtMethod ctMethod = ctClass.getDeclaredMethod(this.methodName);
        CtClass[] parameterTypes = ctMethod.getParameterTypes();
        StringBuilder parameters = new StringBuilder();
        if (parameterTypes.length > 0) {
          parameters.append(",new Object[]{");
          if (Objects.isNull(environmentVariableList)||environmentVariableList.isEmpty()) {
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
        sb.append("if(com.sdefaa.just.mock.common.strategy.MockManager.INSTANCE.shouldMock(\"" + this.className + "\",\"" + this.methodName + "\"");
        sb.append(parameters);
        sb.append(")){");
        sb.append("return ($r)com.sdefaa.just.mock.common.strategy.MockManager.INSTANCE.doMock(\"" + this.className + "\",\"" + this.methodName + "\",$type");
        sb.append(parameters);
        sb.append(");");
        sb.append("}");
        sb.append("}catch(Throwable e){}");
        ctMethod.insertBefore(sb.toString());
        byte[] byteCode = ctClass.toBytecode();
        ctClass.detach();
        Boolean debug = JustMockAgentConfigLoader.INSTANCE.agentConfigProperties().getDebug();
        if (debug) {
          Path byteCodePath = Paths.get(this.className + ".class");
          Files.write(byteCodePath, byteCode);
        }
        return byteCode;
      } catch (Throwable e) {
        MockAgentMain.TRANSFORM_HAS_EXCEPTION.compareAndSet(false,true);
        logger.info("目标类的方法修改异常," + e);
        throw new IllegalClassFormatException("目标类的方法修改异常"+e);
      }
    }
    return null;
  }
}
