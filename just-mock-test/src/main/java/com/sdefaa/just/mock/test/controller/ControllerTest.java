package com.sdefaa.just.mock.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Julius Wong
 * <p>
 * Controller类型测试
 * <p>
 * @since 1.0.0
 */
@Controller
public class ControllerTest {
  @GetMapping("/hello")
  public String say(){
    return null;
  }
}
