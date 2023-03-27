package com.sdefaa.just.mock.dashboard.pojo.dto;

import lombok.Data;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
public class RegisteredApiInfoDTO {
  private String pid;
  private String className;
  private String methodName;
  private String methodArgsDesc;
  private String methodReturnDesc;

  private String methodAnnotationsDesc;
  private String classAnnotationsDesc;
  private String apiUrl;
  private String apiType;
  private String apiMethod;
}
