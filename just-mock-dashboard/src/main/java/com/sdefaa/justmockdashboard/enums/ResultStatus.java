package com.sdefaa.justmockdashboard.enums;

/**
 * @author Julius Wong
 * <p>
 * 1.0.0
 * <p>
 * @since 状态枚举
 */
public enum ResultStatus implements Status {

    /**
     * 成功状态
     */
    SUCCESS("000000", "成功"),
    ATTACH_NOT_SUPPORTED_EXCEPTION("000001", "连接虚拟机异常"),
    AGENT_LOAD_EXCEPTION("000002", "加载代理异常"),
    AGENT_INITIALIZATION_EXCEPTION("000003", "代理初始化异常"),
    IO_EXCEPTION("000004", "IO异常"),
    VM_NOT_EXISTED("000005", "虚拟机实例不存在"),
    VM_DETACH_EXCEPTION("000006", "虚拟机实例卸载异常"),
    VM_INSERT_EXCEPTION("000007", "已连接虚拟机落库异常"),
    VM_INSERT_FAILED("000008", "已连接虚拟机落库失败"),
    /**
     * 失败状态
     */
    SYSTEM_ERROR("999999", "系统异常");
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
