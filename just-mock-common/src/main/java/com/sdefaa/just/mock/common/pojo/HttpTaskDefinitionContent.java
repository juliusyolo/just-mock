package com.sdefaa.just.mock.common.pojo;

import lombok.Data;

/**
 * @author Julius Wong
 * <p>
 * Http任务定义内容
 * <p>
 * @since 1.0.0
 */
@Data
public class HttpTaskDefinitionContent {
    private String url;
    private String payload;
    private String method;

    private String payloadType;

}
