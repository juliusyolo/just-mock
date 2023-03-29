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
public class VMInstanceAttachModel {
    private Long id;
    private String pid;
    private Integer port;
    private String name;
    private String platform;
    private String vendor;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
