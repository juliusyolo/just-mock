package com.sdefaa.just.mock.common.pojo;

import lombok.Data;

/**
 * @author Julius Wong
 * <p>
 * Http任务定义
 * <p>
 * @since 1.0.0
 */
@Data
public class HttpTaskDefinition {
    private String type;
    private HttpTaskDefinitionContent content;
}
