package com.sdefaa.just.mock.test.controller;

import freemarker.cache.StringTemplateLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public freemarker.template.Configuration configuration() {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_22);
        final StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(stringTemplateLoader);
        return configuration;
    }
}
