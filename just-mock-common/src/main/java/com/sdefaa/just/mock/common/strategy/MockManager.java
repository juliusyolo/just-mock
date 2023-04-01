package com.sdefaa.just.mock.common.strategy;

import com.sdefaa.just.mock.common.pojo.RandomVariable;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class MockManager {

    public final static MockManager INSTANCE = new MockManager();
    private final Map<String, AbstractMockStrategy> MOCK_STRATEGY_MAP = new ConcurrentHashMap<>();

    private final Map<String, List<RandomVariable>> MOCK_RANDOM_VARIABLE_MAP = new ConcurrentHashMap<>();

    private final Map<String, List<String>> MOCK_TASK_DEFINITION_POST_PROCESSOR_MAP = new ConcurrentHashMap<>();

    private final Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
    private final StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
    private final static String SPLIT = "#";

    private MockManager() {
        configuration.setTemplateLoader(stringTemplateLoader);
    }

    public boolean shouldMock(String clazzName, String methodName, Object[] parameters) {
        List<RandomVariable> randomVariables = MOCK_RANDOM_VARIABLE_MAP.get(clazzName + SPLIT + methodName);
        return this.MOCK_STRATEGY_MAP.containsKey(clazzName + SPLIT + methodName) && this.MOCK_STRATEGY_MAP.get(clazzName + SPLIT + methodName).canMock(randomVariables, parameters);
    }

    public synchronized void putMock(String clazzName, String methodName, String templateContent, String el, List<RandomVariable> randomVariables, List<String> taskDefinitions) throws IOException {
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
        Optional.ofNullable(taskDefinitions).ifPresent(t -> this.MOCK_TASK_DEFINITION_POST_PROCESSOR_MAP.put(clazzName + SPLIT + methodName, t));
        Optional.ofNullable(randomVariables).ifPresent(r -> this.MOCK_RANDOM_VARIABLE_MAP.put(clazzName + SPLIT + methodName, r));
    }

    public void removeMock(String clazzName, String methodName) {
        stringTemplateLoader.removeTemplate(clazzName + SPLIT + methodName);
        configuration.clearTemplateCache();
        this.MOCK_STRATEGY_MAP.remove(clazzName + SPLIT + methodName);
        this.MOCK_RANDOM_VARIABLE_MAP.remove(clazzName + SPLIT + methodName);
        this.MOCK_TASK_DEFINITION_POST_PROCESSOR_MAP.remove(clazzName + SPLIT + methodName);
    }

    public Object doMock(String clazzName, String methodName, Class<?> returnClazz, Object[] parameters) {
        List<RandomVariable> randomVariables = MOCK_RANDOM_VARIABLE_MAP.get(clazzName + SPLIT + methodName);
        List<String> taskDefinitions = MOCK_TASK_DEFINITION_POST_PROCESSOR_MAP.get(clazzName + SPLIT + methodName);
        return this.MOCK_STRATEGY_MAP.get(clazzName + SPLIT + methodName).mock(returnClazz, taskDefinitions, randomVariables, parameters);
    }
}
