package com.sdefaa.just.mock.common.strategy;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class MockStrategyManager {

  public final static MockStrategyManager INSTANCE = new MockStrategyManager();
  private final Map<String, AbstractMockStrategy> MOCK_STRATEGY_MAP = new ConcurrentHashMap<>();

  private final Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
  private final StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
  private final static String SPLIT = "#";

  private MockStrategyManager() {
    configuration.setTemplateLoader(stringTemplateLoader);
  }

  public boolean shouldMock(String clazzName, String method, Object[] parameters) {
    return this.MOCK_STRATEGY_MAP.containsKey(clazzName + SPLIT + method)&&this.MOCK_STRATEGY_MAP.get(clazzName + SPLIT + method).canMock(parameters);
  }


  public synchronized void addMock(String clazzName, String method,String templateContent,String el) throws IOException {
    stringTemplateLoader.putTemplate(clazzName + SPLIT + method,templateContent);
    configuration.clearTemplateCache();
    Template template = configuration.getTemplate(clazzName + SPLIT + method);
    AbstractMockStrategy abstractMockStrategy;
    if (Objects.nonNull(el)){
      abstractMockStrategy = new ConditionalMockStrategy(template,el);
    }else {
      abstractMockStrategy = new DefaultMockStrategy(template);
    }
    this.MOCK_STRATEGY_MAP.put(clazzName + SPLIT + method, abstractMockStrategy);
  }

  public void modifyMock(String clazzName, String method, String templateContent,String el) throws IOException {
    this.addMock(clazzName,method,templateContent,el);
  }

  public void removeMock(String clazzName, String method) {
    stringTemplateLoader.removeTemplate(clazzName + SPLIT + method);
    configuration.clearTemplateCache();
    this.MOCK_STRATEGY_MAP.remove(clazzName + SPLIT + method);
  }

  public Object doMock(String clazzName, String method,Class<?> returnClazz, Object[] parameter) {
    return this.MOCK_STRATEGY_MAP.get(clazzName + SPLIT + method).mock(returnClazz,parameter);
  }
}
