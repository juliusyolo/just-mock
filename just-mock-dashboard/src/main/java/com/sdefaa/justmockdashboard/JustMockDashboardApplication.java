package com.sdefaa.justmockdashboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan(value = "com.sdefaa.justmockdashboard.mapper")
public class JustMockDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(JustMockDashboardApplication.class, args);
    }

}
