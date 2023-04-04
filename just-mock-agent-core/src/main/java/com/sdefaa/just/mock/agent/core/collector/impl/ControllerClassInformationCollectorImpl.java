package com.sdefaa.just.mock.agent.core.collector.impl;

import com.sdefaa.just.mock.agent.core.collector.AbstractClassInformationCollector;
import com.sdefaa.just.mock.agent.core.collector.ClassInformationCollector;
import com.sdefaa.just.mock.agent.core.enums.ClassMarkEnum;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class ControllerClassInformationCollectorImpl extends AbstractClassInformationCollector {

  @Override
  public boolean match(Class<?> clazz) {

    return !clazz.isInterface()
      && (isClassAnyAnnotated("org.springframework.stereotype.Controller",clazz)
      || isClassAnyAnnotated("org.springframework.stereotype.Controller",clazz.getSuperclass())
      || isClassAnyAnnotated("org.springframework.stereotype.Controller",clazz.getInterfaces()));
  }

  @Override
  public TargetClass collect(Class<?> clazz) {
    TargetClass targetClass = new TargetClass(clazz);


    targetClass.setMark(ClassMarkEnum.CONTROLLER.name());
    return targetClass;
  }
}
