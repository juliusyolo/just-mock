package com.sdefaa.just.mock.common.pojo;

import lombok.Data;

/**
 * @author Julius Wong
 * @date 2023/3/17 15:30
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
