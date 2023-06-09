package com.sdefaa.just.mock.dashboard.pojo.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机实例Model
 * <p>
 * @since 1.0.0
 */
@Data
public class VMInstanceAttachInfoModel {
    private Long id;
    private String pid;
    private String name;
    private String platform;
    private String vendor;
    private String environmentVariables;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
