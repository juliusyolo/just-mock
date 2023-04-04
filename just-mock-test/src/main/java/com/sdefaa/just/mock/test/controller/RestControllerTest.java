package com.sdefaa.just.mock.test.controller;

import com.sdefaa.just.mock.test.pojo.Test1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Julius Wong
 * <p>
 * RestController类测试
 * <p>
 * @since 1.0.0
 */
@RestController
public class RestControllerTest implements FeignTest {
  @Override
  public Test1 hello(Test1 test1) {
    return test1;
  }

  @Override
  public Test1 hello1(Test1 test1) {
    return test1;
  }

  @Override
  public Test1 hello2(Test1 test1) {
    return test1;
  }
}
