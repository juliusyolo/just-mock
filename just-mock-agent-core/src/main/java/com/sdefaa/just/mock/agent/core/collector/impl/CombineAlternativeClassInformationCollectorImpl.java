package com.sdefaa.just.mock.agent.core.collector.impl;

import com.sdefaa.just.mock.agent.core.collector.AbstractClassInformationCollector;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class CombineAlternativeClassInformationCollectorImpl extends AbstractClassInformationCollector {

  public final static String FILTERED_CLASS = "org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController";
  public final static List<AbstractClassInformationCollector> CLASS_INFORMATION_COLLECTOR_LIST = Arrays.asList(new ControllerClassInformationCollectorImpl(), new RestControllerClassInformationCollectorImpl(), new FeignProxyClassInformationCollectorImpl());

  @Override
  public boolean match(Class<?> clazz) {
    return CLASS_INFORMATION_COLLECTOR_LIST.stream().anyMatch(collector -> collector.match(clazz));
  }

  @Override
  public TargetClass collect(Class<?> clazz) {
    if (Objects.equals(clazz.getName(), FILTERED_CLASS)) {
      return null;
    }
    return CLASS_INFORMATION_COLLECTOR_LIST.stream()
      .filter(collector -> collector.match(clazz))
      .findFirst()
      .map(collector -> collector.collect(clazz))
      .orElse(null);
  }
}
