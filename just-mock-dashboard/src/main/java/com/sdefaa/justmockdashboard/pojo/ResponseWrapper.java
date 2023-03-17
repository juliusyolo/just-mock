package com.sdefaa.justmockdashboard.pojo;

import lombok.Data;

/**
 * @author Julius Wong
 * <p>
 * 请求响应包装
 * <p>
 * @since 1.0.0
 */
@Data
public class ResponseWrapper<T>{
    private String code;
    private String message;
    private T data;
}
