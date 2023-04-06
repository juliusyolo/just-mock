package com.sdefaa.just.mock.agent.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetMethod {
  private Method method;
  private String methodName;
  private String mark;
  private String signature;
  private List<String> annotations;

}
