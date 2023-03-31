package com.sdefaa.just.mock.dashboard.pojo.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
public class MockTemplateInfoModel {
  Long id;
  private String el;
  private String templateContent;
  private String tag;
  private String randomVariables;
  private String taskDefinitions;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
