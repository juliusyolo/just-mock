package com.sdefaa.just.mock.common.strategy;

import com.sdefaa.just.mock.common.pojo.RandomVariable;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public interface MockStrategy {
    Object mock(Class<?> returnClass, RandomVariable[] randomVariables, Object[] parameters);

}
