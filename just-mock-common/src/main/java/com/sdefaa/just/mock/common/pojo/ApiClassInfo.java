package com.sdefaa.just.mock.common.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Julius Wong
 * <p>
 *     API所属类信息
 * </p>
 * @since 1.0.0
 */
@Data
public class ApiClassInfo {
  private String className;
  private List<String> annotations;
}
