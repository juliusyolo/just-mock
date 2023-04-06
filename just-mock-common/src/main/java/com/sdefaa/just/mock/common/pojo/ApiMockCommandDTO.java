package com.sdefaa.just.mock.common.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
public class ApiMockCommandDTO {
  private String commandType;
  private String clazzName;
  private String methodName;
  private String templateContent;
  private String el;

  private List<RandomVariable> randomVariables;

  private List<String> taskDefinitions;

  public enum CommandType {
    PUT,
    REMOVE
  }
}
