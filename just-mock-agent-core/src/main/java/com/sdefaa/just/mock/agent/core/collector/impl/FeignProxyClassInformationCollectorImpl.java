package com.sdefaa.just.mock.agent.core.collector.impl;

import com.sdefaa.just.mock.agent.core.collector.AbstractClassInformationCollector;
import com.sdefaa.just.mock.agent.core.constant.CommonConstant;
import com.sdefaa.just.mock.agent.core.enums.ClassMarkEnum;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;
import com.sdefaa.just.mock.agent.core.pojo.TargetMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class FeignProxyClassInformationCollectorImpl extends AbstractClassInformationCollector {

  @Override
  public boolean match(Class<?> clazz) {
    return !clazz.isInterface()
      && isClassAnyAnnotated("org.springframework.cloud.openfeign.FeignClient", clazz.getInterfaces());
  }

  @Override
  public TargetClass collect(Class<?> clazz) {
    TargetClass targetClass = new TargetClass(clazz);
    List<TargetMethod> targetMethods = Arrays
      .stream(clazz.getInterfaces())
      .flatMap(interfaceClazz -> Arrays.stream(interfaceClazz.getDeclaredMethods()))
      .filter(method -> isMethodAnyAnnotated(CommonConstant.TARGET_MVC_MAPPING_CLASS_LIST, method))
      .map(method -> {
        TargetMethod targetMethod = new TargetMethod();
        targetMethod.setAnnotations(Arrays.stream(method.getAnnotations()).map(Annotation::toString).collect(Collectors.toList()));
        targetMethod.setMethodName(method.getName());
        targetMethod.setMark("");
        targetMethod.setSignature(clazz.getName() + "#" + method.getName());
        return targetMethod;
      })
      .collect(Collectors.toList());
    targetClass.setAnnotations(Arrays.stream(clazz.getInterfaces())
      .flatMap(interfaceClazz -> Arrays.stream(interfaceClazz.getAnnotations()))
      .map(Annotation::toString)
      .collect(Collectors.toList()));
    targetClass.setTargetMethods(targetMethods);
    targetClass.setMark(ClassMarkEnum.FEIGN.name());
    return targetClass;
  }
}
