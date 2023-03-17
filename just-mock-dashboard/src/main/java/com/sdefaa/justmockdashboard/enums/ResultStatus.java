package com.sdefaa.justmockdashboard.enums;

/**
 * @author Julius Wong
 * <p>
 * 1.0.0
 * <p>
 * @since 状态枚举
 */
public enum ResultStatus implements Status{

    /**
     * 成功状态
     */
    SUCCESS("000000","成功"),
    /**
     * 失败状态
     */
    SYSTEM_ERROR("999999","系统异常");
    private String code;
    private String message;

    ResultStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
