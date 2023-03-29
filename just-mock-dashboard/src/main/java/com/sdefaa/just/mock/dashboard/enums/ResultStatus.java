package com.sdefaa.just.mock.dashboard.enums;

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
    JSON_PROCESS_EXCEPTION("000009", "Json处理异常"),
    REGISTER_API_FAILED("000010", "注册API失败"),
    CALL_MOCK_COMMAND_EXCEPTION("000011", "远程调用MockCommand异常"),
    CALL_MOCK_COMMAND_FAILED("000011", "远程调用MockCommand失败"),
    QUERY_MOCK_COMMAND_PORT_EXCEPTION("000011", "查询远程调用MockCommand端口异常"),
    UPDATE_MOCK_ENABLE_FLAG_EXCEPTION("000012", "更新数据库Mock标志位为关闭异常"),
    UPDATE_MOCK_ENABLE_FLAG_FAILED("000013", "更新数据库Mock标志位为关闭失败"),
    REMOVE_MOCK_TEMPLATE_EXCEPTION("000014", "移除Mock模板异常"),
    REMOVE_MOCK_TEMPLATE_FAILED("000015", "移除Mock模板失败"),
    PUT_MOCK_TEMPLATE_EXCEPTION("000016", "配置Mock模板异常"),
    PUT_MOCK_TEMPLATE_FAILED("000017", "配置Mock模板失败"),
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
