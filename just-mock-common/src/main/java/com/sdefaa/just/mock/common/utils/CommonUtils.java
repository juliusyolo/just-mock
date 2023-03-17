package com.sdefaa.just.mock.common.utils;

import com.sdefaa.just.mock.common.pojo.ApiClassInfo;
import com.sdefaa.just.mock.common.pojo.ApiInfo;
import com.sdefaa.just.mock.common.pojo.ApiMethodArgInfo;
import com.sdefaa.just.mock.common.pojo.ApiMethodInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Julius Wong
 * @date 2023/3/17 16:31
 * @since 1.0.0
 */
public class CommonUtils {

  public static Stream<Class> generateTargetClass(Class[] classes){
    return Arrays
      .stream(classes)
      .filter(aClass -> aClass.isAnnotationPresent(Override.class));
  }
  public static Stream<ApiInfo> generateApiInfoFromTargetClass(Class clazz){
    return Arrays.stream(clazz.getDeclaredMethods()).filter(method ->
      method.isAnnotationPresent(null)
      &&method.isAnnotationPresent(null)
      &&method.isAnnotationPresent(null)
      &&method.isAnnotationPresent(null)
      &&method.isAnnotationPresent(null)
    ).map(method -> {
      ApiInfo apiInfo = new ApiInfo();
      apiInfo.setApiType("");
      apiInfo.setApiUrl("");
      apiInfo.setApiMethod("");
      ApiClassInfo apiClassInfo = new ApiClassInfo();
      apiClassInfo.setClassName(clazz.getName());
      apiInfo.setApiClassInfo(apiClassInfo);
      ApiMethodInfo apiMethodInfo = new ApiMethodInfo();
      apiMethodInfo.setMethodName(method.getName());
      ApiMethodArgInfo output =new ApiMethodArgInfo();
      output.setType(method.getName());
      output.setJsonStruct("");
      apiMethodInfo.setOutput(output);
      List<ApiMethodArgInfo> inputs = Arrays.stream(method.getParameters()).map(parameter -> {
        ApiMethodArgInfo input = new ApiMethodArgInfo();
        input.setType(parameter.getType().getTypeName());
        input.setJsonStruct("");
        return input;
      }).collect(Collectors.toList());
      apiMethodInfo.setInputs(inputs);
      apiInfo.setApiMethodInfo(apiMethodInfo);
      return apiInfo;
    });
  }
}
