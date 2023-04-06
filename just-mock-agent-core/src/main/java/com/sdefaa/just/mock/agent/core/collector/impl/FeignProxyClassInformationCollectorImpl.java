package com.sdefaa.just.mock.agent.core.collector.impl;

import com.sdefaa.just.mock.agent.core.collector.AbstractClassInformationCollector;
import com.sdefaa.just.mock.agent.core.enums.ClassMarkEnum;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;
import com.sdefaa.just.mock.agent.core.pojo.TargetMethod;
import com.sdefaa.just.mock.agent.core.utils.CollectorUtils;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class FeignProxyClassInformationCollectorImpl extends AbstractClassInformationCollector {

  @Override
  public boolean match(Class<?> clazz) {
   return !clazz.isInterface()
      && CollectorUtils.isClassAnyAnnotated("org.springframework.cloud.openfeign.FeignClient", clazz.getInterfaces());
  }

  @Override
  public TargetClass collect(Class<?> clazz) {
    TargetClass targetClass = new TargetClass(clazz);
    List<TargetMethod> targetMethods = CollectorUtils.generateMVCMappingAnnotatedMethodListFromInterfaces(clazz);
    targetClass.setAnnotations(CollectorUtils.generateAnnotationList(clazz.getInterfaces()));
    targetClass.setTargetMethods(targetMethods);
    targetClass.setMark(ClassMarkEnum.FEIGN.name());
    return targetClass;
  }
}
