package com.sdefaa.justmockdashboard.pojo.dto;

import lombok.Data;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机实例DTO
 * <p>
 * @since 1.0.0
 */
@Data
public class VMInstanceDTO {
    private String pid;
    private String name;
    private String platform;
    private String vendor;
    private Boolean attached;

}
