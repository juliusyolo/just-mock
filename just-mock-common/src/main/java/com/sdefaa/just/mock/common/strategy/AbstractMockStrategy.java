package com.sdefaa.just.mock.common.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.pojo.RandomVariable;
import com.sdefaa.just.mock.common.task.AbstractPostProcessor;
import com.sdefaa.just.mock.common.task.PostProcessorResolver;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public abstract class AbstractMockStrategy implements MockStrategy {

  private final Template template;
  private static final Logger logger = Logger.getLogger(AbstractMockStrategy.class.getName());

  public AbstractMockStrategy(Template template) {
    this.template = template;
  }

  /**
   * 是否开启mock
   *
   * @param parameters mock方法的请求参数
   * @result = true开启，false不开启
   */
  protected abstract boolean canMock(List<RandomVariable> randomVariables, Object[] parameters);

  @Override
  public Object mock(Class<?> returnClass, List<String> taskDefinitions, List<RandomVariable> randomVariables, Object[] parameters) {
    Map<String, Object> modelMap = new HashMap<>();
    if (Objects.nonNull(parameters)) {
      for (int i = 0; i < parameters.length; i++) {
        modelMap.put("p" + i, parameters[i]);
      }
    }
    if (Objects.nonNull(randomVariables)) {
      for (RandomVariable randomVariable : randomVariables) {
        String[] seq = randomVariable.getSequence().split(",");
        int random = new Random().nextInt(seq.length);
        modelMap.put(randomVariable.getName(), seq[random]);
      }
    }
    String str;
    try (StringWriter writer = new StringWriter();) {
      template.process(modelMap, writer);
      str = writer.toString();
    } catch (TemplateException | IOException e) {
      logger.info("模板引擎处理异常，Mock失效," + e);
      throw new RuntimeException(e);
    }
    Object result;
    try {
      if (returnClass == boolean.class || returnClass == Boolean.class) {
        result = Boolean.parseBoolean(str);
      } else if (returnClass == byte.class || returnClass == Byte.class) {
        result = Byte.parseByte(str);
      } else if (returnClass == short.class || returnClass == Short.class) {
        result = Short.parseShort(str);
      } else if (returnClass == int.class || returnClass == Integer.class) {
        result = Integer.parseInt(str);
      } else if (returnClass == long.class || returnClass == Long.class) {
        result = Long.parseLong(str);
      } else if (returnClass == float.class || returnClass == Float.class) {
        result = Float.parseFloat(str);
      } else if (returnClass == double.class || returnClass == Double.class) {
        result = Double.parseDouble(str);
      } else if (returnClass == char.class || returnClass == Character.class) {
        result = str.charAt(0);
      } else if (CharSequence.class.isAssignableFrom(returnClass)) {
        result = str;
      } else if (returnClass.isEnum()) {
        result = Enum.valueOf((Class<Enum>) returnClass, str);
      } else {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        result = objectMapper.readValue(str, returnClass);
      }
    } catch (JsonProcessingException e) {
      logger.info("无法将Mock内容转换对应响应实体，Mock失效," + e);
      throw new RuntimeException(e);
    } catch (Exception e) {
      logger.info("无法将Mock内容转换成对应简单响应实体，Mock失效," + e);
      throw new RuntimeException(e);
    }
    List<AbstractPostProcessor> postProcessors = Optional.ofNullable(taskDefinitions).map(taskDefinitionList -> taskDefinitionList.stream().map(taskDefinition -> PostProcessorResolver.INSTANCE.resolve(taskDefinition, modelMap)).filter(Objects::nonNull).collect(Collectors.toList())).orElse(null);
    Optional.ofNullable(postProcessors).ifPresent(postProcessorList -> postProcessorList.forEach(CompletableFuture::runAsync));
    return result;
  }

}
