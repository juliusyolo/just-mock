package com.sdefaa.just.mock.common.strategy;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.pojo.RandomVariable;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public abstract class AbstractMockStrategy implements MockStrategy{

  private final Template template;
  private static final Logger logger = Logger.getLogger(AbstractMockStrategy.class.getName());
  public AbstractMockStrategy(Template template) {
    this.template = template;
  }

  /**
   * 是否开启mock
   * @param parameters mock方法的请求参数
   * @return true开启，false不开启
   */
  protected abstract boolean canMock(RandomVariable[] randomVariables, Object[] parameters);

  @Override
  public Object mock(Class<?> returnClass, RandomVariable[] randomVariables, Object[] parameters) {
    Map<String,Object> modelMap = new HashMap<>();
    if (Objects.nonNull(parameters)){
      for (int i = 0; i < parameters.length; i++) {
        modelMap.put("p"+i,parameters[i]);
      }
    }
    if (Objects.nonNull(randomVariables)){
      for (RandomVariable randomVariable : randomVariables) {
        String[] seq = randomVariable.getSequence().split(",");
        int random = new Random().nextInt(seq.length);
        modelMap.put(randomVariable.getName(),seq[random]);
      }
    }
    StringWriter writer = new StringWriter();
    try {
      template.process(modelMap,writer);
    } catch (TemplateException | IOException e) {
      logger.info("模板引擎处理异常，Mock失效,"+e);
      throw new RuntimeException(e);
    }
    String str = writer.toString();
    try {
      if (returnClass.isPrimitive()) {
        if (returnClass == boolean.class) {
          return Boolean.parseBoolean(str);
        } else if (returnClass == byte.class) {
          return Byte.parseByte(str);
        } else if (returnClass == short.class) {
          return Short.parseShort(str);
        } else if (returnClass == int.class) {
          return Integer.parseInt(str);
        } else if (returnClass == long.class) {
          return Long.parseLong(str);
        } else if (returnClass == float.class) {
          return Float.parseFloat(str);
        } else if (returnClass == double.class) {
          return Double.parseDouble(str);
        } else if (returnClass == char.class) {
          return str.charAt(0);
        }
      } else if (isWrapperType(returnClass)) {
        if (returnClass == Boolean.class) {
          return Boolean.valueOf(str);
        } else if (returnClass == Byte.class) {
          return Byte.valueOf(str);
        } else if (returnClass == Short.class) {
          return Short.valueOf(str);
        } else if (returnClass == Integer.class) {
          return Integer.valueOf(str);
        } else if (returnClass == Long.class) {
          return Long.valueOf(str);
        } else if (returnClass == Float.class) {
          return Float.valueOf(str);
        } else if (returnClass == Double.class) {
          return Double.valueOf(str);
        } else if (returnClass == Character.class) {
          return str.charAt(0);
        }
      }else if (CharSequence.class.isAssignableFrom(returnClass)) {
        return str;
      }else if (returnClass.isEnum()){
        return Enum.valueOf((Class<Enum>) returnClass, str);
      }
    }catch (Exception e){
      logger.info("无法将Mock内容转换成对应简单响应实体，Mock失效,"+e);
      throw new RuntimeException(e);
    }
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
      return objectMapper.readValue(str, returnClass);
    } catch ( Exception e) {
      logger.info("无法将Mock内容转换对应响应实体，Mock失效,"+e);
      throw new RuntimeException(e);
    }
  }

  public static boolean isWrapperType(Class<?> clazz) {
    return clazz.equals(Boolean.class) ||
      clazz.equals(Byte.class) ||
      clazz.equals(Short.class) ||
      clazz.equals(Integer.class) ||
      clazz.equals(Long.class) ||
      clazz.equals(Float.class) ||
      clazz.equals(Double.class) ||
      clazz.equals(Character.class);
  }

}
