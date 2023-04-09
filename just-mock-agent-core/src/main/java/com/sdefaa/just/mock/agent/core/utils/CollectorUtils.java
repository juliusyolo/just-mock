package com.sdefaa.just.mock.agent.core.utils;

import com.sdefaa.just.mock.agent.core.constant.CommonConstant;
import com.sdefaa.just.mock.agent.core.pojo.TargetMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class CollectorUtils {
    public final static String REST_MAPPING_REGEX = "@org\\.springframework\\.web\\.bind\\.annotation\\.(Post|Get)Mapping.*path=\\[(.*?)\\],.*value=\\[(.*?)\\],.*";
    public final static String REQUEST_MAPPING_REGEX = "@org\\.springframework\\.web\\.bind\\.annotation\\.RequestMapping.*path=\\[(.*?)\\],.*method=\\[(.*?)\\],.*value=\\[(.*?)\\],.*";


    public final static String getMVCMappingMark(List<String> annotationList) {
        String targetAnnotation = annotationList.stream().filter(annotation -> CommonConstant.TARGET_MVC_MAPPING_CLASS_LIST.stream().anyMatch(annotation::contains)).collect(Collectors.joining());
        if (targetAnnotation.contains(CommonConstant.REQUEST_MAPPING_CLASS)) {
            return targetAnnotation.replaceAll(REQUEST_MAPPING_REGEX, "$2 $1$3");
        }
        String method = targetAnnotation.replaceAll(REST_MAPPING_REGEX, "$1 ");
        String path = targetAnnotation.replaceAll(REST_MAPPING_REGEX, "$2$3");
        return method.toUpperCase().concat(path);
    }

    public final static boolean isClassAnyAnnotated(String annotationClazzName, Class<?>... clazzArray) {
        return Arrays.stream(clazzArray)
                .anyMatch(clazz -> {
                    try {
                        Class<?> aClass = clazz.getClassLoader().loadClass(annotationClazzName);
                        return Objects.nonNull(clazz.getAnnotation((Class<? extends Annotation>) aClass));
                    } catch (Exception e) {
                        return false;
                    }
                });
    }

    public final static boolean isMethodAnyAnnotated(Class<?> clazz, List<String> annotationClazzNameList, Method method) {
        return annotationClazzNameList.stream().anyMatch(annotationClazzName -> {
            try {
                Class<?> aClass = clazz.getClassLoader().loadClass(annotationClazzName);
                return Objects.nonNull(method.getAnnotation((Class<? extends Annotation>) aClass));
            } catch (Exception e) {
                return false;
            }
        });
    }

    public final static List<TargetMethod> generateMVCMappingAnnotatedMethodListFromInterfaces(Class<?> clazz) {
        return Arrays.stream(clazz.getInterfaces())
                .flatMap(interfaceClazz -> Arrays.stream(interfaceClazz.getDeclaredMethods()))
                .filter(method -> isMethodAnyAnnotated(clazz, CommonConstant.TARGET_MVC_MAPPING_CLASS_LIST, method))
                .map(CollectorUtils::newTargetMethod)
                .collect(Collectors.toList());
    }

    public final static List<TargetMethod> generateMVCMappingAnnotatedMethodListFromClass(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods()).filter(method -> isMethodAnyAnnotated(clazz, CommonConstant.TARGET_MVC_MAPPING_CLASS_LIST, method))
                .map(CollectorUtils::newTargetMethod)
                .collect(Collectors.toList());
    }

    public final static List<TargetMethod> generateTargetMethodList(Class<?> clazz) {
        List<TargetMethod> list = new ArrayList<>();
        if (Objects.isNull(clazz)) {
            return list;
        }
        List<TargetMethod> targetClassMethodList = generateMVCMappingAnnotatedMethodListFromClass(clazz);
        List<TargetMethod> targetInterfaceMethodList = generateMVCMappingAnnotatedMethodListFromInterfaces(clazz);
        list.addAll(targetClassMethodList);
        list.addAll(targetInterfaceMethodList);
        list.addAll(generateTargetMethodList(clazz.getSuperclass()));
        return list;
    }

    public final static TargetMethod newTargetMethod(Method method) {
        TargetMethod targetMethod = new TargetMethod();
        targetMethod.setAnnotations(Arrays.stream(method.getAnnotations()).map(Annotation::toString).collect(Collectors.toList()));
        targetMethod.setMethodName(method.getName());
        targetMethod.setMethod(method);
        targetMethod.setMark(CollectorUtils.getMVCMappingMark(targetMethod.getAnnotations()));
        targetMethod.setSignature(method.getName() + Arrays.toString(method.getParameterTypes()));
        return targetMethod;
    }

    public final static List<String> generateAnnotationList(Class<?>... classes) {
        return Arrays.stream(classes)
                .flatMap(interfaceClazz -> Arrays.stream(interfaceClazz.getAnnotations()))
                .map(Annotation::toString)
                .collect(Collectors.toList());
    }
}
