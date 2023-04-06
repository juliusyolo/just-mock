package com.sdefaa.just.mock.common.pojo;

import lombok.Data;

/**
 * @author Julius Wong
 * <p>
 * API信息
 * </p>
 * @since 1.0.0
 */
@Data
public class ApiInfo {
  private String apiUrl;
  private String apiMethod;
  private String apiType;
  private ApiClassInfo apiClassInfo;
  private ApiMethodInfo apiMethodInfo;


}
