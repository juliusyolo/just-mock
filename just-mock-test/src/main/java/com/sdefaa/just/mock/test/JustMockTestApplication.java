package com.sdefaa.just.mock.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JustMockTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JustMockTestApplication.class, args);
    }

}
