package com.sdefaa.just.mock.agent.core.collector;

import com.sdefaa.just.mock.agent.core.collector.impl.CombineAlternativeClassInformationCollectorImpl;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class ClassInformationCollectorManager {
  private static final Logger logger = Logger.getLogger(ClassInformationCollectorManager.class.getName());

  public final static List<AbstractClassInformationCollector> CLASS_INFORMATION_COLLECTOR_LIST = Arrays.asList(new CombineAlternativeClassInformationCollectorImpl());

  public final static Stream<TargetClass> collectTargetClassStream(Class<?> clazz) {
    return CLASS_INFORMATION_COLLECTOR_LIST.stream().map(collector -> {
      if (collector.match(clazz)) {
        logger.info("ClassInformationCollectorManager matched clazz:" + clazz.getName());
        return collector.collect(clazz);
      }
      return null;
    }).filter(Objects::nonNull).filter(targetClass -> !targetClass.getTargetMethods().isEmpty());
  }
}
