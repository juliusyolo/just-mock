package com.sdefaa.just.mock.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Configuration
public class TestConfig {

  @Autowired
  @Qualifier("com.sdefaa.just.mock.test.controller.FeignTest")
  FeignTest feignTest;
}
