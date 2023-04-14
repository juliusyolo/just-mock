package com.sdefaa.just.mock.agent.transformer;

import com.sdefaa.just.mock.agent.MockAgentMain;
import com.sdefaa.just.mock.agent.config.JustMockAgentConfigLoader;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;
import com.sdefaa.just.mock.agent.core.pojo.TargetMethod;
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

    private final TargetClass targetClass;
    private final List<String> environmentVariableList;

    public MockClassFileTransformer(TargetClass targetClass, List<String> environmentVariableList) {
        this.targetClass = targetClass;
        this.environmentVariableList = environmentVariableList;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        String targetClassName = this.targetClass.getClazz().getName();
        if (className.equals(targetClassName.replace('.', '/'))) {
            CtClass ctClass = null;
            try {
                ClassPool pool = new ClassPool();
                pool.insertClassPath(new LoaderClassPath(loader));
                ctClass = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
                for (TargetMethod targetMethod : this.targetClass.getTargetMethods()) {
                    CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethod.getMethodName());
                    boolean isVoidReturnType = Objects.equals(ctMethod.getReturnType().getName(), "void");
                    CtClass[] parameterTypes = ctMethod.getParameterTypes();
                    StringBuilder parameters = new StringBuilder();
                    String parameterSequence = generateParameterSequence(parameterTypes);
                    String environmentVariableSequence = generateEnvironmentVariableSequence(environmentVariableList);
                    if (Objects.isNull(parameterSequence) || Objects.isNull(environmentVariableSequence)){
                      if (Objects.isNull(parameterSequence) && Objects.isNull(environmentVariableSequence)){
                        parameters.append(",null");
                      }else if (Objects.isNull(parameterSequence)){
                        parameters.append(",new Object[]{").append(environmentVariableSequence).append("}");
                      }else {
                        parameters.append(",new Object[]{").append(parameterSequence).append("}");
                      }
                    }else {
                      parameters.append(",new Object[]{").append(parameterSequence).append(",").append(environmentVariableSequence).append("}");
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("try{");
                    sb.append("if(com.sdefaa.just.mock.common.strategy.MockManager.INSTANCE.shouldMock(\"" + targetClassName + "\",\"" + targetMethod.getMethodName() + "\"");
                    sb.append(parameters);
                    sb.append(")){");
                    if (isVoidReturnType) {
                        sb.append("com.sdefaa.just.mock.common.strategy.MockManager.INSTANCE.doMock(\"" + targetClassName + "\",\"" + targetMethod.getMethodName() + "\",java.lang.Class.forName(\""+generateReturnTypeName(ctMethod.getReturnType())+"\")");
                    } else {
                        sb.append("return (" + ctMethod.getReturnType().getName() + ")com.sdefaa.just.mock.common.strategy.MockManager.INSTANCE.doMock(\"" + targetClassName + "\",\"" + targetMethod.getMethodName() + "\",java.lang.Class.forName(\""+generateReturnTypeName(ctMethod.getReturnType())+"\")");
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
                  CtClass.debugDump = "./dump";
                }
                return byteCode;
            } catch (Throwable e) {
                MockAgentMain.TRANSFORM_FILED_CLASS.add(targetClass);
                logger.info("目标类的方法修改异常,类名:"+targetClassName+",异常:" + e);
            }
        }
        return null;
    }

    public String generateParameterSequence(CtClass[] parameterTypes){
      StringBuilder sequences = new StringBuilder();
      if (parameterTypes.length > 0) {
        for (int i = 0; i < parameterTypes.length; i++) {
          if (parameterTypes[i].isPrimitive()){
            sequences.append(wrapPrimitiveType(parameterTypes[i],i+1));
          }else {
            sequences.append("(Object)$" + (i+1) + ",");
          }
        }
        return sequences.substring(0, sequences.length()-1);
      }
      return null;
    }

    public String wrapPrimitiveType(CtClass ctClass,int index){
      String className = ctClass.getName();
      String result;
      switch (className){
        case "int": result = "(Object)Integer.valueOf($"+index+"),";break;
        case "short": result = "(Object)Short.valueOf($"+index+"),";break;
        case "long": result = "(Object)Long.valueOf($"+index+"),";break;
        case "double": result = "(Object)Double.valueOf($"+index+"),";break;
        case "float": result = "(Object)Float.valueOf($"+index+"),";break;
        case "byte": result = "(Object)Byte.valueOf($"+index+"),";break;
        case "char": result = "(Object)Character.valueOf($"+index+"),";break;
        default: result = "(Object)Boolean.valueOf($"+index+"),";
      }
      return result;
    }

    public String generateEnvironmentVariableSequence(List<String> environmentVariableList){
      StringBuilder sequences = new StringBuilder();
      if (Objects.nonNull(environmentVariableList) && !environmentVariableList.isEmpty()) {
        for (int i = 0; i < environmentVariableList.size(); i++) {
          sequences.append("(Object)" + environmentVariableList.get(i) + ",");
        }
        return sequences.substring(0, sequences.length() - 1);
      }
      return null;
    }

    public String generateReturnTypeName(CtClass ctClass){
      String className = ctClass.getName();
      String result;
      switch (className){
        case "int": result = "java.lang.Integer";break;
        case "short": result = "java.lang.Short";break;
        case "long": result = "java.lang.Long";break;
        case "double": result = "java.lang.Double";break;
        case "float": result = "java.lang.Float";break;
        case "byte": result = "java.lang.Byte";break;
        case "char": result = "java.lang.Character";break;
        case "boolean": result = "java.lang.Boolean";break;
        case "void": result = "java.lang.Void";break;
        default: result = className;
      }
      return result;
    }
}
