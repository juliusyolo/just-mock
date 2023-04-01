package com.sdefaa.just.mock.dashboard.pojo;

import com.sdefaa.just.mock.dashboard.enums.Status;
import lombok.Data;

/**
 * @author Julius Wong
 * <p>
 * 请求响应包装
 * <p>
 * @since 1.0.0
 */
@Data
public class ResponseWrapper<T> {
    private String code;
    private String message;
    private T data;

    public static <T> ResponseWrapper<T> wrap(Status status, T data) {
        ResponseWrapper<T> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setCode(status.getCode());
        responseWrapper.setMessage(status.getMessage());
        responseWrapper.setData(data);
        return responseWrapper;
    }

    public static <T> ResponseWrapper<T> wrap(Status status) {
        return wrap(status, null);
    }
}
