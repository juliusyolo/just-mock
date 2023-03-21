package com.sdefaa.just.mock.agent.transformer;

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

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class MockClassFileTransformer implements ClassFileTransformer {

  private final String className;
  private final String methodName;

  public MockClassFileTransformer(String className, String methodName) {
    this.className = className;
    this.methodName = methodName;
  }

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    if (className.equals(this.className.replace('.', '/'))) {
      try {
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
        CtMethod ctMethod = ctClass.getDeclaredMethod(this.methodName);
        StringBuilder sb = new StringBuilder();
        sb.append("if(com.sdefaa.just.mock.common.strategy.MockStrategyManager.INSTANCE.shouldMock(\""+this.className+"\",\""+this.methodName+"\")){");
        sb.append("return ($r)com.sdefaa.just.mock.common.strategy.MockStrategyManager.INSTANCE.doMock(\""+this.className+"\",\""+this.methodName+"\",$type");
        CtClass[] parameterTypes = ctMethod.getParameterTypes();
        if (parameterTypes.length>0){
          sb.append(",new Object[]{");
          for (int i = 1; i < parameterTypes.length; i++) {
            sb.append("(Object)$"+i+",");
          }
          sb.append("(Object)$"+parameterTypes.length);
          sb.append("}");
        }else {
          sb.append(",null");
        }
        sb.append(");");
        sb.append("}");
        ctMethod.insertBefore(sb.toString());
        byte[] byteCode = ctClass.toBytecode();
        ctClass.detach();
        Path byteCodePath = Paths.get(this.className+ ".class");
        Files.write(byteCodePath, byteCode);
        return byteCode;
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
