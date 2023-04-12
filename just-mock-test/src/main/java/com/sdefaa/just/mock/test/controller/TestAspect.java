package com.sdefaa.just.mock.test.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
//@Aspect
//@Component
public class TestAspect {

  @Pointcut("execution(* com.sdefaa.just.mock.test.controller.ControllerTest.*(..))")
  public void pointcut(){}

  @Around("pointcut()")
  public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
    return joinPoint.proceed();
  }
}
