package com.sdefaa.just.mock.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.enums.ApiTypeEnum;
import com.sdefaa.just.mock.common.pojo.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.ServerSocket;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Julius Wong
 * <p>
 * 公共工具类
 * </p>
 * @since 1.0.0
 */
public class CommonUtils {

  private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private final static String FEIGN_TARGET_CLASS = "@org.springframework.cloud.openfeign.FeignClient";
  private final static List<String> TARGET_CLASSES = Arrays.asList("@org.springframework.stereotype.Controller", "@org.springframework.web.bind.annotation.RestController", FEIGN_TARGET_CLASS);
  private final static String REQUEST_MAPPING_TARGET_METHOD = "@org.springframework.web.bind.annotation.RequestMapping";
  public final static List<String> TARGET_METHODS = Arrays.asList("@org.springframework.web.bind.annotation.PostMapping", "@org.springframework.web.bind.annotation.GetMapping", "@org.springframework.web.bind.annotation.PutMapping", "@org.springframework.web.bind.annotation.PatchMapping", "@org.springframework.web.bind.annotation.DeleteMapping", REQUEST_MAPPING_TARGET_METHOD);

  public final static List<String> FILTERED_CONTROLLERS = Arrays.asList("org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController");
  public static Stream<ApiInfo> generateApiInfoFromTargetClass(TargetClass targetClass) {
    String apiType = generateApiType(targetClass.getAnnotations());
    return targetClass.getTargetMethods().stream().map(targetMethod -> {
      ApiInfo apiInfo = new ApiInfo();
      apiInfo.setApiType(apiType);
      apiInfo.setApiUrl(generateApiUrl(targetMethod.getAnnotations()));
      apiInfo.setApiMethod(generateApiMethod(targetMethod.getAnnotations()));
      ApiClassInfo apiClassInfo = new ApiClassInfo();
      apiClassInfo.setAnnotations(targetClass.getAnnotations());
      apiClassInfo.setClassName(targetClass.getClazz().getName());
      apiInfo.setApiClassInfo(apiClassInfo);
      ApiMethodInfo apiMethodInfo = new ApiMethodInfo();
      apiMethodInfo.setMethodName(targetMethod.getMethod().getName());
      apiMethodInfo.setAnnotations(targetMethod.getAnnotations());
      ApiMethodArgInfo output = new ApiMethodArgInfo();
      output.setType(targetMethod.getMethod().getReturnType().getName());
      output.setJsonStruct(generateClassStruct(targetMethod.getMethod().getReturnType()));
      apiMethodInfo.setOutput(output);
      List<ApiMethodArgInfo> inputs = Arrays.stream(targetMethod.getMethod().getParameters()).map(parameter -> {
        ApiMethodArgInfo input = new ApiMethodArgInfo();
        input.setType(parameter.getType().getTypeName());
        input.setJsonStruct(generateClassStruct(parameter.getType()));
        return input;
      }).collect(Collectors.toList());
      apiMethodInfo.setInputs(inputs);
      apiInfo.setApiMethodInfo(apiMethodInfo);
      return apiInfo;
    });
  }

  public static TargetClass generateTargetClass(Class clazz) {
    if (!hasTargetClassAnnotation(clazz)||FILTERED_CONTROLLERS.contains(clazz.getName())){
      return null;
    }
    TargetClass targetClass = null;
    List<Method> methods = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> Modifier.isPublic(method.getModifiers())).collect(Collectors.toList());
    if (currentClassHasTargetClassAnnotation(clazz)) {
      targetClass = new TargetClass(clazz);
      List<String> annotationsInClass = Arrays.stream(clazz.getAnnotations()).map(Annotation::toString).collect(Collectors.toList());
      targetClass.getAnnotations().addAll(annotationsInClass);
      List<TargetMethod> methodsInClass = methods.stream()
        .filter(CommonUtils::hasTargetMethodAnnotation)
        .map(method -> new TargetMethod(method, method.getName() + Arrays.toString(method.getParameterTypes()), Arrays.stream(method.getAnnotations()).map(Annotation::toString).collect(Collectors.toList())))
        .collect(Collectors.toList());
      targetClass.getTargetMethods().addAll(methodsInClass);
    }
    if (superClassHasTargetClassAnnotation(clazz)) {
      if (Objects.isNull(targetClass)) {
        targetClass = new TargetClass(clazz);
      }
      List<String> annotationsInSuperClass = Arrays.stream(clazz.getSuperclass().getAnnotations()).map(Annotation::toString).collect(Collectors.toList());
      targetClass.getAnnotations().addAll(annotationsInSuperClass);
      List<TargetMethod> targetMethodsInSuperClass = Arrays.stream(clazz.getSuperclass().getDeclaredMethods())
        .filter(method -> Modifier.isPublic(method.getModifiers()))
        .filter(method -> methods.stream().anyMatch(m -> CommonUtils.methodEquals(method, m)))
        .filter(CommonUtils::hasTargetMethodAnnotation)
        .map(method -> new TargetMethod(method, method.getName() + Arrays.toString(method.getParameterTypes()), Arrays.stream(method.getAnnotations()).map(Annotation::toString).collect(Collectors.toList())))
        .collect(Collectors.toList());
      targetClass.getTargetMethods().addAll(targetMethodsInSuperClass);
    }
    if (superInterfaceHasTargetClassAnnotation(clazz)) {
      if (Objects.isNull(targetClass)) {
        targetClass = new TargetClass(clazz);
      }
      List<String> annotationsInSuperInterfaces = Arrays.stream(clazz.getInterfaces()).flatMap(aClass -> Arrays.stream(aClass.getAnnotations())).map(Annotation::toString).collect(Collectors.toList());
      targetClass.getAnnotations().addAll(annotationsInSuperInterfaces);
      List<TargetMethod> targetMethodsInSuperInterfaces = Arrays.stream(clazz.getInterfaces())
        .flatMap(aClass -> Arrays.stream(aClass.getDeclaredMethods()))
        .filter(CommonUtils::hasTargetMethodAnnotation)
        .map(method -> new TargetMethod(method, method.getName() + Arrays.toString(method.getParameterTypes()), Arrays.stream(method.getAnnotations()).map(Annotation::toString).collect(Collectors.toList())))
        .collect(Collectors.toList());
      targetClass.getTargetMethods().addAll(targetMethodsInSuperInterfaces);
    }
    if (Objects.nonNull(targetClass)) {
      List<String> distinctAnnotationsInClass = targetClass.getAnnotations().stream().distinct().collect(Collectors.toList());
      targetClass.setAnnotations(distinctAnnotationsInClass);
      List<TargetMethod> distinctMethodsInClass = new ArrayList<>();
      targetClass.getTargetMethods().stream().collect(Collectors.groupingBy(TargetMethod::getSignature)).forEach((key, value) -> {
        if (value.size() == 1) {
          distinctMethodsInClass.addAll(value);
        } else {
          List<String> distinctAnnotationInMethod = value.stream().flatMap(targetMethod -> targetMethod.getAnnotations().stream()).distinct().collect(Collectors.toList());
          distinctMethodsInClass.add(new TargetMethod(value.get(0).getMethod(),value.get(0).getSignature(),distinctAnnotationInMethod));
        }
      });
      targetClass.setTargetMethods(distinctMethodsInClass);
    }
    return targetClass;
  }

  public static boolean methodEquals(Method m1, Method m2) {
    return Objects.equals(m1.getName() + Arrays.toString(m1.getParameterTypes()), m2.getName() + Arrays.toString(m2.getParameterTypes()));
  }

  public static boolean hasTargetClassAnnotation(Class clazz) {
    return !clazz.isInterface() && (currentClassHasTargetClassAnnotation(clazz) || superClassHasTargetClassAnnotation(clazz) || superInterfaceHasTargetClassAnnotation(clazz));
  }

  public static boolean currentClassHasTargetClassAnnotation(Class clazz) {
    return Arrays.stream(clazz.getAnnotations()).map(Annotation::toString).map(s -> s.replaceAll("(.*)\\(.*\\)", "$1")).anyMatch(TARGET_CLASSES::contains);
  }

  public static boolean superClassHasTargetClassAnnotation(Class clazz) {
    if (Objects.isNull(clazz.getAnnotatedSuperclass())) {
      return false;
    }
    return Arrays.stream(clazz.getAnnotatedSuperclass().getAnnotations()).map(Annotation::toString).map(s -> s.replaceAll("(.*)\\(.*\\)", "$1")).anyMatch(TARGET_CLASSES::contains);
  }

  public static boolean superInterfaceHasTargetClassAnnotation(Class clazz) {
    return Arrays.stream(clazz.getInterfaces()).flatMap(aClass -> Arrays.stream(aClass.getAnnotations())).map(Annotation::toString).map(s -> s.replaceAll("(.*)\\(.*\\)", "$1")).anyMatch(TARGET_CLASSES::contains);
  }

  public static boolean hasTargetMethodAnnotation(Method method) {
    return Arrays.stream(method.getAnnotations()).map(Annotation::toString).map(s -> s.replaceAll("(.*)\\(.*\\)", "$1")).anyMatch(CommonUtils.TARGET_METHODS::contains);
  }

  private static String generateClassStruct(Class clazz) {
    if (clazz.isPrimitive() || Character.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz) || clazz.isEnum() || clazz == String.class) {
      return clazz.getName();
    }
    Map<String, String> map = new HashMap<>();
    Arrays.stream(clazz.getDeclaredFields()).forEach(field -> map.put(field.getName(), generateClassStruct(field.getType())));
    try {
      return OBJECT_MAPPER.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      return map.toString();
    }
  }

  private static String generateApiUrl(List<String> annotations) {
    return annotations.stream().collect(Collectors.joining(";"));
  }

  private static String generateApiMethod(List<String> annotations) {
    String annotationStr = annotations.stream().collect(Collectors.joining(";"));
    if (annotationStr.contains(REQUEST_MAPPING_TARGET_METHOD)) {
      String httpMethod = annotationStr.replaceAll(".*method=\\{(\\w*)\\}.*", "$1");
      if (Objects.equals(httpMethod, "")) {
        return "NONE";
      }
      return httpMethod.toUpperCase();
    }
    return annotationStr.replaceAll(".*\\.(.*)Mapping.*", "$1").toUpperCase();
  }

  private static String generateApiType(List<String> annotations) {
    boolean match = annotations.stream().map(s -> s.replaceAll("(.*)\\(.*\\)", "$1")).anyMatch(s -> Objects.equals(s, FEIGN_TARGET_CLASS));
    if (match) {
      return ApiTypeEnum.FEIGN_API.name();
    }
    return ApiTypeEnum.CONTROLLER_API.name();
  }

  public static int getAvailablePort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }
}
