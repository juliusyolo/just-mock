package com.sdefaa.just.mock.test;

import com.sdefaa.just.mock.test.controller.FeignTest;
import com.sdefaa.just.mock.test.controller.RestControllerTest;
import feign.Feign;
import org.junit.jupiter.api.Test;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.Opcodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class JustMockTestApplicationTests {
    @Autowired
    FeignTest feignTest;
    @Test
    void contextLoads() throws IOException {
        Class<?> clazz = feignTest.getClass();
        ClassReader reader = new ClassReader(clazz.getName());
        ClassWriter writer = new ClassWriter(reader, 0);
        ClassVisitor visitor = new MyClassVisitor(writer);
        reader.accept(visitor, 0);
        Files.write(Paths.get("E:\\Workspace\\IdeaProjects\\creative-explorer-all\\just-mock\\just-mock-test\\a.class"), writer.toByteArray());
    }

    private static class MyClassVisitor extends ClassVisitor {
        public MyClassVisitor(ClassWriter writer) {
            super(Opcodes.ASM7, writer);
        }
    }


}
