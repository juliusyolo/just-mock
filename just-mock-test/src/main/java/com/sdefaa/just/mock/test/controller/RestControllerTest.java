package com.sdefaa.just.mock.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Julius Wong
 * <p>
 * RestController类测试
 * <p>
 * @since 1.0.0
 */
@Component
public class RestControllerTest  {
    @Autowired
    FeignTest feignTest;
    public String test(){
       return feignTest.hello();
    }
}
