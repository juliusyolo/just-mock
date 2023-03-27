package com.sdefaa.just.mock.common.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Julius Wong
 * <p>
 *     API所属方法信息
 * </p>
 * @since 1.0.0
 */
@Data
public class ApiMethodInfo {
  private String methodName;
  private List<String> annotations;
  private ApiMethodArgInfo output;
  private List<ApiMethodArgInfo> inputs;

}
