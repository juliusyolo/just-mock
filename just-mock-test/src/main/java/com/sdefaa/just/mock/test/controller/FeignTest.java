package com.sdefaa.just.mock.test.controller;

import feign.Feign;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

/**
 * @author Julius Wong
 * <p>
 * Feign类型测试
 * <p>
 * @since 1.0.0
 */
@FeignClient(name = "just-mock",url="http://localhost:8080")
public interface FeignTest {

     @GetMapping("/hello")
     String hello();

}
