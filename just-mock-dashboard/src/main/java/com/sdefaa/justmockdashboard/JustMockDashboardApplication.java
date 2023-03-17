package com.sdefaa.justmockdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class JustMockDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(JustMockDashboardApplication.class, args);
    }

}
