package com.sdefaa.justmockdashboard.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Julius Wong
 * <p>
 *
 * <p>
 * @since 1.0.0
 */
@Aspect
@Component
public class JustMockControllerAspect {

    @Pointcut(value = "execution(com.sdefaa.justmockdashboard.controller.VMInstanceController)")
    public void pointcut(){}


}
