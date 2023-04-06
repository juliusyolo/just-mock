package com.sdefaa.just.mock.common.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.enums.TaskTypeEnum;
import com.sdefaa.just.mock.common.pojo.HttpTaskDefinition;
import com.sdefaa.just.mock.common.pojo.TaskDefinition;
import com.sdefaa.just.mock.common.task.impl.HttpTaskPostProcessor;

import java.util.Map;
import java.util.Objects;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class PostProcessorResolver {

  public final static PostProcessorResolver INSTANCE = new PostProcessorResolver();

  public final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private PostProcessorResolver() {

  }

  public AbstractPostProcessor resolve(String taskDefinition, Map<String, Object> model) {
    try {
      TaskDefinition taskDef = OBJECT_MAPPER.readValue(taskDefinition, TaskDefinition.class);
      if (Objects.equals(TaskTypeEnum.HTTP_TASK.name(), taskDef.getType())) {
        return new HttpTaskPostProcessor(OBJECT_MAPPER.readValue(taskDefinition, HttpTaskDefinition.class), model);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}
