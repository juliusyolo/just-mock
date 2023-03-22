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

    public boolean shouldMock(String clazzName, String methodName, Object[] parameters) {
        return this.MOCK_STRATEGY_MAP.containsKey(clazzName + SPLIT + methodName) && this.MOCK_STRATEGY_MAP.get(clazzName + SPLIT + methodName).canMock(parameters);
    }


    public synchronized void addMock(String clazzName, String methodName, String templateContent, String el) throws IOException {
        stringTemplateLoader.putTemplate(clazzName + SPLIT + methodName, templateContent);
        configuration.clearTemplateCache();
        Template template = configuration.getTemplate(clazzName + SPLIT + methodName);
        AbstractMockStrategy abstractMockStrategy;
        if (Objects.nonNull(el)) {
            abstractMockStrategy = new ConditionalMockStrategy(template, el);
        } else {
            abstractMockStrategy = new DefaultMockStrategy(template);
        }
        this.MOCK_STRATEGY_MAP.put(clazzName + SPLIT + methodName, abstractMockStrategy);
    }

    public void modifyMock(String clazzName, String methodName, String templateContent, String el) throws IOException {
        this.addMock(clazzName, methodName, templateContent, el);
    }

    public void removeMock(String clazzName, String methodName) {
        stringTemplateLoader.removeTemplate(clazzName + SPLIT + methodName);
        configuration.clearTemplateCache();
        this.MOCK_STRATEGY_MAP.remove(clazzName + SPLIT + methodName);
    }

    public Object doMock(String clazzName, String methodName, Class<?> returnClazz, Object[] parameter) {
        return this.MOCK_STRATEGY_MAP.get(clazzName + SPLIT + methodName).mock(returnClazz, parameter);
    }
}
