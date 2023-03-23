package com.sdefaa.just.mock.common.strategy;

import freemarker.template.Template;

import javax.el.*;
import java.util.Objects;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class ConditionalMockStrategy extends AbstractMockStrategy {

  private final String el;

  public ConditionalMockStrategy(Template template, String el) {
    super(template);
    this.el = el;
  }

  @Override
  protected boolean canMock(Object... parameters) {
    if (Objects.isNull(parameters)) {
      return true;
    }
    ExpressionFactory factory = ExpressionFactory.newInstance();
    ELContext elContext = new StandardELContext(factory);
    VariableMapper variableMapper = elContext.getVariableMapper();
    for (int i = 0; i < parameters.length; i++) {
      variableMapper.setVariable("p" + i, factory.createValueExpression(parameters[i], Object.class));
    }
    ValueExpression valueExpression = factory.createValueExpression(elContext, el, Boolean.class);
    return (boolean) valueExpression.getValue(elContext);
  }

}
