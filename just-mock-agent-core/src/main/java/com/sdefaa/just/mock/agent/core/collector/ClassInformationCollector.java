package com.sdefaa.just.mock.agent.core.collector;


import com.sdefaa.just.mock.agent.core.pojo.TargetClass;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public interface ClassInformationCollector {
  boolean match(Class<?> clazz);

  TargetClass collect(Class<?> clazz);

}
