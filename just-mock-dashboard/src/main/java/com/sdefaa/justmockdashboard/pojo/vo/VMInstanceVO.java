package com.sdefaa.justmockdashboard.pojo.vo;

import lombok.Data;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机实例VO
 * <p>
 * @since 1.0.0
 */
@Data
public class VMInstanceVO {
    private String pid;
    private String name;
    private String platform;
    private String vendor;
    private Boolean attached;

}
