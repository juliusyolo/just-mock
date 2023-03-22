package com.sdefaa.justmockdashboard.config;

import com.sdefaa.justmockdashboard.exception.GlobalException;
import com.sdefaa.justmockdashboard.pojo.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Julius Wong
 * <p>
 *  统一的全局异常处理
 * <p>
 * @since 1.0.0
 */
@RestControllerAdvice
@Slf4j
public class JustMockControllerAdvice {

    @ExceptionHandler(value = GlobalException.class)
    public ResponseWrapper<Void> globalExceptionHandle(GlobalException e) {
        log.error("全局异常打印",e);
        ResponseWrapper<Void> response = new ResponseWrapper<>();
        response.setCode(e.getCode());
        response.setMessage(e.getMessage());
        return response;
    }
}
