package com.sdefaa.just.mock.dashboard.pojo.dto;

import lombok.Data;

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
}
