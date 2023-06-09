package com.sdefaa.just.mock.agent.transformer;

import com.sdefaa.just.mock.agent.MockAgentMain;
import com.sdefaa.just.mock.agent.config.JustMockAgentConfigLoader;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;
import com.sdefaa.just.mock.agent.core.pojo.TargetMethod;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.*;
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
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        String targetClassName = this.targetClass.getClazz().getName();
        if (className.equals(targetClassName.replace('.', '/'))) {
            try {
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM9, classWriter) {
                    @Override
                    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                        if (shouldTransformMethod(name)) {
                            return new MockMethodVisitor(Opcodes.ASM9, methodVisitor, targetClassName, name, isMethodStatic(name), descriptor, environmentVariableList);
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
            } catch (Throwable e) {
                MockAgentMain.TRANSFORM_FILED_CLASS.add(targetClass);
                e.printStackTrace();
            }

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

    private boolean isMethodStatic(String methodName) {
        return this.targetClass
                .getTargetMethods()
                .stream()
                .filter(targetMethod -> Objects.equals(targetMethod.getMethodName(), methodName))
                .anyMatch(targetMethod -> Modifier.isStatic(targetMethod.getMethod().getModifiers()));
    }

    static class MockMethodVisitor extends MethodVisitor {
        private final String targetClassName;
        private final String methodName;
        private final boolean isMethodStatic;
        private final String methodDescriptor;
        private final List<String> environmentVariableList;

      Label startTry;
      Label endTry;
      Label catchStart;
      Label catchEnd;
        public MockMethodVisitor(int api, MethodVisitor methodVisitor, String targetClassName, String methodName, boolean isMethodStatic, String methodDescriptor, List<String> environmentVariableList) {
            super(api, methodVisitor);
            this.targetClassName = targetClassName;
            this.methodName = methodName;
            this.isMethodStatic = isMethodStatic;
            this.methodDescriptor = methodDescriptor;
            this.environmentVariableList = Optional.ofNullable(environmentVariableList).orElse(Collections.emptyList());
        }


        @Override
        public void visitCode() {
            Type[] parameterTypes = Type.getArgumentTypes(methodDescriptor);
            int sizeSum = Arrays.stream(parameterTypes).mapToInt(Type::getSize).sum();
            int argumentCount = isMethodStatic ? sizeSum : sizeSum + 1;
            int arrayLength = parameterTypes.length + environmentVariableList.size();
            visitIntInsn(Opcodes.BIPUSH, arrayLength);
            visitTypeInsn(Opcodes.ANEWARRAY, Type.getDescriptor(Object.class));
            visitVarInsn(Opcodes.ASTORE, argumentCount);
            int index = 0;
            int offset = isMethodStatic ? 0 : 1;
            for (Type type : parameterTypes) {
                visitVarInsn(Opcodes.ALOAD, argumentCount);
                visitInsn(Opcodes.ICONST_0 + index);
                visitVarInsn(Opcodes.ALOAD, offset);
                visitInsn(Opcodes.AASTORE);
                index += 1;
                offset += type.getSize();
            }
            for (String variable : environmentVariableList) {
                int dotIndex = variable.lastIndexOf(".");
                int methodStartIndex = variable.indexOf("(");
                visitVarInsn(Opcodes.ALOAD, argumentCount);
                visitInsn(Opcodes.ICONST_0 + index);
                visitMethodInsn(Opcodes.INVOKESTATIC, variable.substring(0, dotIndex).replace(".", "/"), variable.substring(dotIndex + 1, methodStartIndex), "()V", false);
                visitInsn(Opcodes.AASTORE);
                index += 1;
            }
            startTry = new Label();
            endTry = new Label();
            catchStart = new Label();
            catchEnd = new Label();
            Label ifLabel = new Label();
            Type returnType = Type.getReturnType(methodDescriptor);
            visitTryCatchBlock(startTry, endTry, catchStart, "java/lang/Exception");
            visitLabel(startTry);
            visitLdcInsn(this.targetClassName);
            visitLdcInsn(this.methodName);
            visitVarInsn(Opcodes.ALOAD, argumentCount);
            visitMethodInsn(Opcodes.INVOKESTATIC, "com/sdefaa/just/mock/common/strategy/MockManager", "shouldMock", "(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)Z", false);
            visitJumpInsn(Opcodes.IFEQ, ifLabel);
            visitLdcInsn(this.targetClassName);
            visitLdcInsn(this.methodName);
            visitLdcInsn(returnType);
            visitVarInsn(Opcodes.ALOAD, argumentCount);
            visitMethodInsn(Opcodes.INVOKESTATIC, "com/sdefaa/just/mock/common/strategy/MockManager", "doMock", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;", false);
            int returnTypeSort = returnType.getSort();
            switch (returnTypeSort) {
                case Type.VOID:
                    visitInsn(Opcodes.RETURN);
                    visitLabel(endTry);
                    break;
                case Type.BOOLEAN:
                case Type.CHAR:
                case Type.BYTE:
                case Type.SHORT:
                case Type.INT:
                    visitLabel(endTry);
                    visitInsn(Opcodes.IRETURN);
                    break;
                case Type.FLOAT:
                    visitLabel(endTry);
                    visitInsn(Opcodes.FRETURN);
                    break;
                case Type.LONG:
                    visitLabel(endTry);
                    visitInsn(Opcodes.LRETURN);
                    break;
                case Type.DOUBLE:
                    visitLabel(endTry);
                    visitInsn(Opcodes.DRETURN);
                    break;
                default:
                    visitTypeInsn(Opcodes.CHECKCAST, returnType.getInternalName());
                    visitLabel(endTry);
                    visitInsn(Opcodes.ARETURN);
                    break;
            }
            visitFrame(Opcodes.F_NEW, 0, null, 0, null);
            visitLabel(ifLabel);
            visitJumpInsn(Opcodes.GOTO,catchEnd);
            visitLabel(catchStart);
            visitFrame(Opcodes.F_NEW, 0, null, 1, new Object[] {"java/lang/Exception"});
            visitVarInsn(Opcodes.ASTORE, offset);
            visitLabel(catchEnd);
            visitFrame(Opcodes.F_NEW, 0, null, 0, null);
            super.visitCode();
        }

      @Override
      public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack+2, maxLocals+1);
      }
    }
}
