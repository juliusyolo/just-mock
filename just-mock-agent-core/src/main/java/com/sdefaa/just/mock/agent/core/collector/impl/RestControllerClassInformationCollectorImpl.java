package com.sdefaa.just.mock.agent.core.collector.impl;

import com.sdefaa.just.mock.agent.core.collector.AbstractClassInformationCollector;
import com.sdefaa.just.mock.agent.core.enums.ClassMarkEnum;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class RestControllerClassInformationCollectorImpl extends AbstractClassInformationCollector {
  @Override
  public boolean match(Class<?> clazz) {
    return  !clazz.isInterface()
      && (isClassAnyAnnotated("org.springframework.web.bind.annotation.RestController",clazz)
      || isClassAnyAnnotated("org.springframework.web.bind.annotation.RestController",clazz.getSuperclass())
      || isClassAnyAnnotated("org.springframework.web.bind.annotation.RestController",clazz.getInterfaces()));
  }

  @Override
  public TargetClass collect(Class<?> clazz) {
    TargetClass targetClass = new TargetClass(clazz);


    targetClass.setMark(ClassMarkEnum.REST_CONTROLLER.name());
    return targetClass;
  }
}
