package com.sdefaa.just.mock.common.strategy;

import com.sdefaa.just.mock.common.pojo.RandomVariable;
import com.sdefaa.just.mock.common.task.AbstractPostProcessor;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public interface MockStrategy {
    Object mock(Class<?> returnClass, List<AbstractPostProcessor> postProcessor, List<RandomVariable> randomVariables, Object[] parameters);

}
