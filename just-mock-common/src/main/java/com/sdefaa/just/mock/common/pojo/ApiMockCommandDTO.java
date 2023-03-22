package com.sdefaa.just.mock.common.pojo;

import lombok.Data;

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

  public enum CommandType{
    ADD,
    MODIFY,
    REMOVE
  }
}
