package com.sdefaa.just.mock.common.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
public class ApiMethodInfo {
  private String methodName;
  private ApiMethodArgInfo output;
  private List<ApiMethodArgInfo> inputs;

}
