package com.sdefaa.just.mock.common.strategy;

import com.sdefaa.just.mock.common.pojo.RandomVariable;
import freemarker.template.Template;

import javax.el.*;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
  protected boolean canMock(List<RandomVariable> randomVariables, Object[] parameters) {
    if (Objects.isNull(randomVariables) && Objects.isNull(parameters)) {
      return true;
    }
    try {
      ExpressionFactory factory = ExpressionFactory.newInstance();
      ELContext elContext = new StandardELContext(factory);
      VariableMapper variableMapper = elContext.getVariableMapper();
      if (Objects.nonNull(parameters)) {
        for (int i = 0; i < parameters.length; i++) {
          variableMapper.setVariable("p" + i, factory.createValueExpression(parameters[i], Object.class));
        }
      }
      if (Objects.nonNull(randomVariables)) {
        for (RandomVariable randomVariable : randomVariables) {
          String[] seq = randomVariable.getSequence().split(",");
          int random = new Random().nextInt(seq.length);
          variableMapper.setVariable(randomVariable.getName(), factory.createValueExpression(seq[random], Object.class));
        }
      }
      ValueExpression valueExpression = factory.createValueExpression(elContext, el, Boolean.class);
      return (boolean) valueExpression.getValue(elContext);
    } catch (Exception e) {
      return false;
    }

  }

}
