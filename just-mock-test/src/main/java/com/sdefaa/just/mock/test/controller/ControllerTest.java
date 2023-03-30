package com.sdefaa.just.mock.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Julius Wong
 * <p>
 * Controller类型测试
 * <p>
 * @since 1.0.0
 */
@RestController
public class ControllerTest {
  public final static ThreadLocal<Object> threadLocal = ThreadLocal.withInitial(()-> "hello");

  @GetMapping("/hello1")
  public Integer say(){
//    com.sdefaa.just.mock.test.controller.ControllerTest.threadLocal.get()
    return 1;
  }

  @GetMapping("/hello2/test")
  public Test say1(@RequestBody Test test){
    return test;
  }

  public static class Test{
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
  }

}
