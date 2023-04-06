package com.sdefaa;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class Test {
  private String name;
  private String age;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "Test{" +
      "name='" + name + '\'' +
      ", age='" + age + '\'' +
      '}';
  }
}
