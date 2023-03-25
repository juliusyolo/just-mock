package com.sdefaa.just.mock.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.enums.ApiTypeEnum;
import com.sdefaa.just.mock.common.pojo.ApiClassInfo;
import com.sdefaa.just.mock.common.pojo.ApiInfo;
import com.sdefaa.just.mock.common.pojo.ApiMethodArgInfo;
import com.sdefaa.just.mock.common.pojo.ApiMethodInfo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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

    public static Stream<Class> generateTargetClass(Class[] classes) {
        return Arrays.stream(classes).filter(CommonUtils::hasTargetClassAnnotation);
    }

    public static Stream<ApiInfo> generateApiInfoFromTargetClass(Class clazz) {
        String apiType = generateApiType(clazz);
        return Arrays.stream(clazz.getDeclaredMethods()).filter(CommonUtils::hasTargetMethodAnnotation).map(method -> {
            ApiInfo apiInfo = new ApiInfo();
            apiInfo.setApiType(apiType);
            apiInfo.setApiUrl(generateApiUrl(method));
            apiInfo.setApiMethod(generateApiMethod(method));
            ApiClassInfo apiClassInfo = new ApiClassInfo();
            apiClassInfo.setClassName(clazz.getName());
            apiInfo.setApiClassInfo(apiClassInfo);
            ApiMethodInfo apiMethodInfo = new ApiMethodInfo();
            apiMethodInfo.setMethodName(method.getName());
            ApiMethodArgInfo output = new ApiMethodArgInfo();
            output.setType(method.getReturnType().getName());
            output.setJsonStruct(generateClassStruct(method.getReturnType()));
            apiMethodInfo.setOutput(output);
            List<ApiMethodArgInfo> inputs = Arrays.stream(method.getParameters()).map(parameter -> {
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

  public static boolean hasTargetClassAnnotation(Class clazz){
    return Arrays.stream(clazz.getAnnotations()).map(Annotation::toString).map(s -> s.replaceAll("(.*)\\(.*\\)", "$1")).anyMatch(TARGET_CLASSES::contains);
  }

    public static boolean hasTargetMethodAnnotation(Method method){
      return Arrays.stream(method.getAnnotations()).map(Annotation::toString).map(s -> s.replaceAll("(.*)\\(.*\\)", "$1")).anyMatch(CommonUtils.TARGET_METHODS::contains);
    }

    private static String generateClassStruct(Class clazz) {
        if (clazz.isPrimitive()||Character.class.isAssignableFrom(clazz) ||Boolean.class.isAssignableFrom(clazz)||Number.class.isAssignableFrom(clazz)|| clazz.isEnum() || clazz == String.class) {
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

    private static String generateApiUrl(Method method) {
        return Arrays.stream(method.getAnnotations()).map(Annotation::toString).collect(Collectors.joining(";"));
    }

    private static String generateApiMethod(Method method) {
        String annotationStr = Arrays.stream(method.getAnnotations()).map(Annotation::toString).collect(Collectors.joining(";"));
        if (annotationStr.contains(REQUEST_MAPPING_TARGET_METHOD)) {
            String httpMethod =  annotationStr.replaceAll(".*method=\\{(\\w*)\\}.*", "$1");
            if (Objects.equals(httpMethod,"")){
                return "NONE";
            }
            return httpMethod.toUpperCase();
        }
        return annotationStr.replaceAll(".*\\.(.*)Mapping.*", "$1").toUpperCase();
    }

    private static String generateApiType(Class clazz) {
        boolean match = Arrays.stream(clazz.getAnnotations()).map(Annotation::toString).map(s -> s.replaceAll("(.*)\\(.*\\)", "$1")).anyMatch(s -> Objects.equals(s, FEIGN_TARGET_CLASS));
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
