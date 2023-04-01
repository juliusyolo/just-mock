package com.sdefaa.just.mock.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Julius Wong
 * <p>
 * Controller类型测试
 * <p>
 * @since 1.0.0
 */
@RestController
public class ControllerTest {
    public final static ThreadLocal<Object> threadLocal = ThreadLocal.withInitial(() -> "hello");

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/hello1")
    public Test say(@RequestBody Test test) throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(test));
//    com.sdefaa.just.mock.test.controller.ControllerTest.threadLocal.get()
        System.out.println("回调:" + test.toString());
        return test;
    }

    @GetMapping("/hello2")
    public Integer say2(@RequestParam("age") String age, @RequestParam("name") String name) throws JsonProcessingException {
//    com.sdefaa.just.mock.test.controller.ControllerTest.threadLocal.get()
        System.out.println("回调:" + age + "," + name);
        return 1;
    }

    @PostMapping("/hello3")
    public Integer say3(@RequestParam("age") String age, @RequestParam("name") String name) throws JsonProcessingException {
//    com.sdefaa.just.mock.test.controller.ControllerTest.threadLocal.get()
        System.out.println("回调:" + age + "," + name);
        return 1;
    }

    @GetMapping("/hello2/test")
    public Test say1(@RequestBody Test test) {
        return test;
    }

    public static class Test {
        private String name;
        private String age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Test{" +
                    "name='" + name + '\'' +
                    ", age='" + age + '\'' +
                    '}';
        }
    }

}
