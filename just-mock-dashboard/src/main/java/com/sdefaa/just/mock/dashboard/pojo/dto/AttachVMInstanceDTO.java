package com.sdefaa.just.mock.dashboard.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
public class AttachVMInstanceDTO {
    private String pid;
    private List<String> environmentVariables;
}
