package com.sdefaa.just.mock.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

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
    @Qualifier("com.sdefaa.just.mock.test.controller.FeignTest")
  FeignTest feignTest;
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
    public void say3(@RequestParam("age") String age, @RequestParam("name") String name) throws JsonProcessingException {
//    com.sdefaa.just.mock.test.controller.ControllerTest.threadLocal.get()
        System.out.println("回调:" + age + "," + name);
    }

//    @GetMapping("/hello2/test")
    @RequestMapping(value = "/hello2/test",method = RequestMethod.GET)
    public ResponseEntity<Test> say1(@RequestBody Test test) {
        return new ResponseEntity<>(test, HttpStatus.OK);
    }

//    public static class Test {
//        private String name;
//        private String age;
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getAge() {
//            return age;
//        }
//
//        public void setAge(String age) {
//            this.age = age;
//        }
//
//        @Override
//        public String toString() {
//            return "Test{" +
//                    "name='" + name + '\'' +
//                    ", age='" + age + '\'' +
//                    '}';
//        }
//
//      public static void main(String[] args) {
//        System.out.println(Object.class.getSuperclass());
//        System.out.println(ControllerTest.class.getSuperclass().getName());
//        System.out.println(ControllerTest.class.getInterfaces().length);
//        String regex = "@org\\.springframework\\.web\\.bind\\.annotation\\.(Post|Get)Mapping.*path=\\[(.*?)\\],.*value=\\[(.*?)\\],.*";
//        String regex1 = "@org\\.springframework\\.web\\.bind\\.annotation\\.RequestMapping.*path=\\[(.*?)\\],.*method=\\[(.*?)\\],.*value=\\[(.*?)\\],.*";
//
//        String s1 = "@org.springframework.web.bind.annotation.GetMapping(headers=[], path=[], produces=[], name=, params=[], value=[/hello1/{pid}/hello], consumes=[])";
//        System.out.println(s1.replaceAll(regex1, "$1"));
//        System.out.println(s1.replaceAll(regex1,"$2"));
//        Arrays.stream(ControllerTest.class.getDeclaredMethods()).flatMap(method -> Arrays.stream(method.getAnnotations())).map(Annotation::toString).forEach(s -> {
//          System.out.println(s.replaceAll(regex,"$1 $2$3"));
//          System.out.println(s.replaceAll(regex1,"$2 $1$3"));
//        });
//      }
//    }

}
