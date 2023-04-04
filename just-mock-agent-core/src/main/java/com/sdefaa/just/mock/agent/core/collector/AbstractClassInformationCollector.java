package com.sdefaa.just.mock.agent.core.collector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public abstract class AbstractClassInformationCollector implements ClassInformationCollector{

    protected  boolean isClassAnyAnnotated(String annotationClazzName,Class<?> ...clazzArray){
      return Arrays.stream(clazzArray)
        .anyMatch(clazz -> {
          try {
            Class<?> aClass = Class.forName(annotationClazzName);
            return Objects.nonNull(clazz.getAnnotation((Class<? extends Annotation>)aClass));
          }catch (Exception e){
            return false;
          }
        });
    }

    protected  boolean isMethodAnyAnnotated(List<String> annotationClazzNameList, Method method){
      return  annotationClazzNameList.stream().anyMatch(annotationClazzName -> {
          try {
            Class<?> aClass = Class.forName(annotationClazzName);
            return Objects.nonNull(method.getAnnotation((Class<? extends Annotation>) aClass));
          } catch (Exception e) {
            return false;
          }
        });
    }

}
