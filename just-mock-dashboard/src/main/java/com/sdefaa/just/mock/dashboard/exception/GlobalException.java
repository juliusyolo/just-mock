package com.sdefaa.just.mock.dashboard.exception;

import com.sdefaa.just.mock.dashboard.enums.Status;

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
        this.message = message;
    }

    public GlobalException(Status status, Throwable cause) {
        this(status.getCode(), status.getMessage(), cause);
    }

    public GlobalException(Status status) {
      super(status.getMessage());
      this.code = status.getCode();
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
