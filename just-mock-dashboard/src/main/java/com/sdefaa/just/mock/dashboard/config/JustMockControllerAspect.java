package com.sdefaa.just.mock.dashboard.config;

import com.sdefaa.just.mock.dashboard.enums.ResultStatus;
import com.sdefaa.just.mock.dashboard.pojo.ResponseWrapper;
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
