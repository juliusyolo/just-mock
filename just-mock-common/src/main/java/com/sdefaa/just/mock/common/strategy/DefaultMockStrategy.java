package com.sdefaa.just.mock.common.strategy;

import com.github.javafaker.Faker;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.el.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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
