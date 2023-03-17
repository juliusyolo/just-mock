package com.sdefaa.justmockdashboard.exception;

import com.sdefaa.justmockdashboard.enums.Status;

/**
 * @author Julius Wong
 * <p>
 * 全局异常
 * <p>
 * @since 1.0.0
 */
public class GlobalException extends RuntimeException {
    private String code;
    private String message;

    public GlobalException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public GlobalException(Status status, Throwable cause) {
        this(status.getCode(), status.getMessage(), cause);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
