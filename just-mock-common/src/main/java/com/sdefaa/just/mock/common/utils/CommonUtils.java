package com.sdefaa.just.mock.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Julius Wong
 * <p>
 * 公共工具类
 * </p>
 * @since 1.0.0
 */
public class CommonUtils {

  private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static String generateClassStruct(Class clazz) {
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

  public static int getAvailablePort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }
}
