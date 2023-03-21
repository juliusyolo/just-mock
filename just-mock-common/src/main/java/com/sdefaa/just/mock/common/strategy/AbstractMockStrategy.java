package com.sdefaa.just.mock.common.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public abstract class AbstractMockStrategy implements MockStrategy{

  private final Template template;

  public AbstractMockStrategy(Template template) {
    this.template = template;
  }

  protected abstract boolean canMock(Object... parameters);

  @Override
  public Object mock(Class<?> returnClass, Object... parameters) {
    Map<String,Object> modelMap = new HashMap<>();
    if (Objects.nonNull(parameters)){
      for (int i = 0; i < parameters.length; i++) {
        modelMap.put("p"+i,parameters[i]);
      }
    }
    Faker faker = new Faker();
    Map<String,Object> fakerMap = new HashMap<>();
    Map<String,Object> address = new HashMap<>();
    address.put("city",faker.address().city());
    address.put("buildingNumber",faker.address().buildingNumber());
    address.put("cityName",faker.address().cityName());
    address.put("cityPrefix",faker.address().cityPrefix());
    address.put("citySuffix",faker.address().citySuffix());
    address.put("country",faker.address().country());
    address.put("countryCode",faker.address().countryCode());
    address.put("firstName",faker.address().firstName());
    address.put("lastName",faker.address().lastName());
    fakerMap.put("address",address);
    modelMap.put("faker",fakerMap);
    StringWriter writer = new StringWriter();
    try {
      template.process(modelMap,writer);
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(writer.toString(), returnClass);
    } catch (TemplateException | IOException e) {
      return null;
    }
  }
}
