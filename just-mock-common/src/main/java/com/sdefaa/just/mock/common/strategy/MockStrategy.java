package com.sdefaa.just.mock.common.strategy;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public interface MockStrategy {
    Object mock(Class<?> returnClass,Object... parameter);

}
