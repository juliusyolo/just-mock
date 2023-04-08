package com.sdefaa.just.mock.test.controller;

import com.sdefaa.just.mock.test.pojo.Test1;

/**
 * @author Julius Wong
 * <p>
 * RestController类测试
 * <p>
 * @since 1.0.0
 */
public class RestControllerTest implements FeignTest {

  @Override
  public Test1 hello(Test1 test1) {
    return test1;
  }
}
