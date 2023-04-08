package com.sdefaa.just.mock.agent.transformer;

import com.sdefaa.just.mock.agent.config.JustMockAgentConfigLoader;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;
import com.sdefaa.just.mock.agent.core.pojo.TargetMethod;
import org.objectweb.asm.*;

import java.io.IOException;
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
public class MockClassFileASMTransformer implements ClassFileTransformer {
  private static final Logger logger = Logger.getLogger(MockClassFileASMTransformer.class.getName());

  private final TargetClass targetClass;
  private final List<String> environmentVariableList;

  public MockClassFileASMTransformer(TargetClass targetClass, List<String> environmentVariableList) {
    this.targetClass = targetClass;
    this.environmentVariableList = environmentVariableList;
  }


  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    String targetClassName = this.targetClass.getClazz().getName();
    if (className.equals(targetClassName.replace('.', '/'))) {
      ClassReader classReader = new ClassReader(classfileBuffer);
      ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
      ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM7, classWriter) {
        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
          MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
          if (shouldTransformMethod(name)) {
            return new MockMethodVisitor(methodVisitor, targetClassName, name, descriptor, environmentVariableList);
          } else {
            return methodVisitor;
          }
        }
      };
      classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
      Boolean debug = JustMockAgentConfigLoader.INSTANCE.agentConfigProperties().getDebug();
      if (debug) {
        Path byteCodePath = Paths.get(targetClassName + ".class");
        try {
          Files.write(byteCodePath, classWriter.toByteArray());
        } catch (IOException e) {

        }
      }
      return classWriter.toByteArray();
    }
    return null;
  }

  private boolean shouldTransformMethod(String methodName) {
    for (TargetMethod targetMethod : this.targetClass.getTargetMethods()) {
      if (targetMethod.getMethodName().equals(methodName)) {
        return true;
      }
    }
    return false;
  }

  static class MockMethodVisitor extends MethodVisitor {
    private final String targetClassName;
    private final String methodName;
    private final String methodDescriptor;
    private final List<String> environmentVariableList;

    public MockMethodVisitor(MethodVisitor methodVisitor, String targetClassName, String methodName, String methodDescriptor, List<String> environmentVariableList) {
      super(Opcodes.ASM7, methodVisitor);
      this.targetClassName = targetClassName;
      this.methodName = methodName;
      this.methodDescriptor = methodDescriptor;
      this.environmentVariableList = environmentVariableList;
    }

    @Override
    public void visitCode() {
      super.visitCode();
      boolean isVoidReturnType = methodDescriptor.endsWith(")V");
      StringBuilder parameters = new StringBuilder();
      Type[] parameterTypes = Type.getArgumentTypes(methodDescriptor);
      if (parameterTypes.length > 0) {
        parameters.append(",new Object[]{");
        if (Objects.isNull(environmentVariableList) || environmentVariableList.isEmpty()) {
          for (int i = 0; i < parameterTypes.length - 1; i++) {
            parameters.append("(Object)$" + (i + 1) + ",");
          }
          parameters.append("(Object)$" + parameterTypes.length);
        } else {
          for (int i = 0; i < parameterTypes.length; i++) {
            parameters.append("(Object)$" + (i + 1) + ",");
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
      Label label = new Label();
      String mockManagerInternalName = "com/sdefaa/just/mock/common/strategy/MockManager";
      String shouldMockMethodName = "shouldMock";
      String doMockMethodName = "doMock";
      String returnTypeDescriptor = methodDescriptor.substring(methodDescriptor.indexOf(')') + 1);
      if (isVoidReturnType) { //
        // void return type
        visitMethodInsn(Opcodes.INVOKESTATIC, mockManagerInternalName, shouldMockMethodName, "(Ljava/lang/String;Ljava/lang/String;)Z", false);
        visitJumpInsn(Opcodes.IFEQ, label);
        visitMethodInsn(Opcodes.INVOKESTATIC, mockManagerInternalName, doMockMethodName, "(Ljava/lang/String;Ljava/lang/String;" + methodDescriptor + "[Ljava/lang/Object;)V", false);
        visitLabel(label);
      } else { // non-void return type
        Type returnType = Type.getReturnType(methodDescriptor);
        Label returnLabel = new Label();
        int returnOpcode = returnType.getOpcode(Opcodes.IRETURN);
        visitMethodInsn(Opcodes.INVOKESTATIC, mockManagerInternalName, shouldMockMethodName, "(Ljava/lang/String;Ljava/lang/String;)Z", false);
        visitJumpInsn(Opcodes.IFEQ, label);
        visitMethodInsn(Opcodes.INVOKESTATIC, mockManagerInternalName, doMockMethodName, "(Ljava/lang/String;Ljava/lang/String;" + methodDescriptor + "[Ljava/lang/Object;)Ljava/lang/Object;", false);
        if (returnType.getSort() == Type.OBJECT || returnType.getSort() == Type.ARRAY) {
          visitTypeInsn(Opcodes.CHECKCAST, returnType.getInternalName());
        }
        visitInsn(returnOpcode);
        visitLabel(label);
        if (returnType.getSort() == Type.OBJECT || returnType.getSort() == Type.ARRAY) {
          visitInsn(Opcodes.ACONST_NULL);
          visitLabel(returnLabel);
          visitLocalVariable("returnValue", returnTypeDescriptor, null, label, returnLabel, 1);
          visitMaxs(0, 0);
        } else {
          visitLocalVariable("returnValue", returnTypeDescriptor, null, label, returnLabel, 1);
          visitMaxs(0, 0);
        }
      }
    }
  }
}
