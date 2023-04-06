package com.sdefaa.just.mock.agent.core.collector.impl;

import com.sdefaa.just.mock.agent.core.collector.AbstractClassInformationCollector;
import com.sdefaa.just.mock.agent.core.enums.ClassMarkEnum;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;
import com.sdefaa.just.mock.agent.core.pojo.TargetMethod;
import com.sdefaa.just.mock.agent.core.utils.CollectorUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class RestControllerClassInformationCollectorImpl extends AbstractClassInformationCollector {
  private final static String TARGET_REST_CONTROLLER_ANNOTATION_CLASS = "org.springframework.web.bind.annotation.RestController";

  @Override
  public boolean match(Class<?> clazz) {
   return !clazz.isInterface()
      && (CollectorUtils.isClassAnyAnnotated(TARGET_REST_CONTROLLER_ANNOTATION_CLASS, clazz)
      || CollectorUtils.isClassAnyAnnotated(TARGET_REST_CONTROLLER_ANNOTATION_CLASS, clazz.getSuperclass())
      || CollectorUtils.isClassAnyAnnotated(TARGET_REST_CONTROLLER_ANNOTATION_CLASS, clazz.getInterfaces()));
  }

  @Override
  public TargetClass collect(Class<?> clazz) {
    TargetClass targetClass = new TargetClass(clazz);
    List<TargetMethod> targetMethods = CollectorUtils.generateTargetMethodList(clazz).stream().filter(targetMethod -> Arrays.stream(clazz.getDeclaredMethods()).anyMatch(method -> Objects.equals(targetMethod.getSignature(), method.getName() + Arrays.toString(method.getParameterTypes())))).collect(Collectors.toList());
    targetClass.setTargetMethods(targetMethods);
    if (CollectorUtils.isClassAnyAnnotated(TARGET_REST_CONTROLLER_ANNOTATION_CLASS, clazz)) {
      targetClass.setAnnotations(CollectorUtils.generateAnnotationList(clazz));
    } else if (CollectorUtils.isClassAnyAnnotated(TARGET_REST_CONTROLLER_ANNOTATION_CLASS, clazz.getSuperclass())) {
      targetClass.setAnnotations(CollectorUtils.generateAnnotationList(clazz.getSuperclass()));
    } else {
      targetClass.setAnnotations(CollectorUtils.generateAnnotationList(clazz.getInterfaces()));
    }
    targetClass.setMark(ClassMarkEnum.REST_CONTROLLER.name());
    return targetClass;
  }
}
