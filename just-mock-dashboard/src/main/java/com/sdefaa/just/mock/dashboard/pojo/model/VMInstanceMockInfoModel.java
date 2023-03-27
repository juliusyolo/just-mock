package com.sdefaa.just.mock.dashboard.pojo.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Julius Wong
 * @date 2023/3/17 14:42
 * @since 1.0.0
 */
@Data
public class VMInstanceMockInfoModel {
  private Long id;
  private String pid;
  private String className;
  private String methodName;
  private String methodArgsDesc;
  private String methodReturnDesc;
  private String apiUrl;
  private String apiType;
  private String apiMethod;
  private String methodAnnotationsDesc;
  private String classAnnotationsDesc;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
