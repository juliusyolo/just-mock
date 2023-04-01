package com.sdefaa.just.mock.dashboard.pojo.dto;

import com.sdefaa.just.mock.common.pojo.RandomVariable;
import lombok.Data;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
public class PutMockDTO {
  private String pid;

  private Long mockTemplateId;
  private String className;
  private String methodName;
  private String templateContent;
  private String el;
  private List<String> taskDefinitions;
  private List<RandomVariable> randomVariables;
  private String mockTemplateSnapshot;
}
