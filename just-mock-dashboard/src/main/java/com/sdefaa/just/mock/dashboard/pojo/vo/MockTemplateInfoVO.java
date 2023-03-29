package com.sdefaa.just.mock.dashboard.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
public class MockTemplateInfoVO {
  private Long id;
  private String el;
  private String templateContent;
  private String tag;
}
