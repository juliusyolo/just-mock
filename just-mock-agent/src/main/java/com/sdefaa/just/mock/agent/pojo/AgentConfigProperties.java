package com.sdefaa.just.mock.agent.pojo;

import lombok.Data;

/**
 * @author Julius Wong
 * <p>
 * 代理配置属性类
 * <p>
 * @since 1.0.0
 */
@Data
public class AgentConfigProperties {
    private String registryUrl;
    private Boolean debug;
}
