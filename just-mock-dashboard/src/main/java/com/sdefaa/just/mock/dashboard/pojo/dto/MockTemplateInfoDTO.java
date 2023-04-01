package com.sdefaa.just.mock.dashboard.pojo.dto;

import com.sdefaa.just.mock.common.pojo.RandomVariable;
import lombok.Data;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
public class MockTemplateInfoDTO {
    private Long id;
    private String el;
    private String templateContent;
    private String tag;
    private List<RandomVariable> randomVariables;
    private List<String> taskDefinitions;

}
