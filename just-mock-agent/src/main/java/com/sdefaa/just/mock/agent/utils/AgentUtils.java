package com.sdefaa.just.mock.agent.utils;

import com.sdefaa.just.mock.common.utils.CommonUtils;
import javassist.CtMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class AgentUtils {
  public static boolean hasTargetAnnotation(Method method){
      return Arrays.stream(method.getAnnotations()).map(Annotation::toString).map(s -> s.replaceAll("(.*)\\(.*\\)", "$1")).anyMatch(CommonUtils.TARGET_METHODS::contains);
  }
}
