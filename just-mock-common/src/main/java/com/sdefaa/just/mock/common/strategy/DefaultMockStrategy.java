package com.sdefaa.just.mock.common.strategy;

import com.sdefaa.just.mock.common.pojo.RandomVariable;
import freemarker.template.Template;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class DefaultMockStrategy extends AbstractMockStrategy {


  public DefaultMockStrategy(Template template) {
    super(template);
  }

  @Override
  protected boolean canMock(List<RandomVariable> randomVariables, Object[] parameters) {
    return true;
  }


}
