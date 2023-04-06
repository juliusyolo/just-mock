package com.sdefaa.just.mock.agent.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TargetClass {
  private List<String> annotations;
  private Class clazz;

  private String mark;

  private List<TargetMethod> targetMethods;

  public TargetClass(Class clazz) {
    this.clazz = clazz;
    this.annotations = new ArrayList<>();
    this.targetMethods = new ArrayList<>();
  }
}
