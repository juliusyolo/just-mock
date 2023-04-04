package com.sdefaa.just.mock.test.controller;


import com.sdefaa.just.mock.test.pojo.Test1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    Test1 hello(@RequestBody Test1 test1);

    @GetMapping("/hello1/feign")
    Test1 hello1(@RequestBody Test1 test1);

    @GetMapping("/hello2/feign")
    Test1 hello2(@RequestBody Test1 test1);
}
