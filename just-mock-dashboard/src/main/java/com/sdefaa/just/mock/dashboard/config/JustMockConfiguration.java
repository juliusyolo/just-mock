package com.sdefaa.just.mock.dashboard.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Julius Wong
 * <p>
 * 配置类
 * <p>
 * @since 1.0.0
 */
@Configuration
public class JustMockConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
