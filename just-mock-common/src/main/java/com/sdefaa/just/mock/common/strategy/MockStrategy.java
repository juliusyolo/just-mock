package com.sdefaa.just.mock.common.strategy;

import com.sdefaa.just.mock.common.pojo.RandomVariable;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public interface MockStrategy {
  Object mock(Class<?> returnClass, List<String> taskDefinitions, List<RandomVariable> randomVariables, Object[] parameters);

}
