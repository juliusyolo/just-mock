package com.sdefaa.just.mock.common.strategy;

import freemarker.template.Template;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class DefaultMockStrategy extends AbstractMockStrategy{


  public DefaultMockStrategy(Template template) {
    super(template);
  }

  @Override
  protected boolean canMock(Object... parameters) {
    return true;
  }


}
