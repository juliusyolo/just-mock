package com.sdefaa.just.mock.dashboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan(value = "com.sdefaa.just.mock.dashboard.mapper")
public class JustMockDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(JustMockDashboardApplication.class, args);
    }

}
