package com.sdefaa.justmockdashboard.config;

import com.sdefaa.justmockdashboard.enums.ResultStatus;
import com.sdefaa.justmockdashboard.pojo.ResponseWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Julius Wong
 * <p>
 *
 * <p>
 * @since 1.0.0
 */

public class JustMockControllerAspect {

    @Pointcut(value = "execution(public * com.sdefaa.justmockdashboard.controller..*.*(..))")
    public void pointcut(){}


    @Around(value = "pointcut()")
    public Object doResponseWrap(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      Object proceed = proceedingJoinPoint.proceed();
      ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>();
      responseWrapper.setData(proceed);
      responseWrapper.setCode(ResultStatus.SUCCESS.getCode());
      responseWrapper.setMessage(ResultStatus.SUCCESS.getMessage());
      return responseWrapper;
    }


}
