package com.sdefaa.just.mock.test.controller;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Julius Wong
 * <p>
 * Feign类型测试
 * <p>
 * @since 1.0.0
 */
@FeignClient(name = "just-mock", url = "http://localhost:8080")
public interface FeignTest {

    @GetMapping("/hello")
    String hello();

}
