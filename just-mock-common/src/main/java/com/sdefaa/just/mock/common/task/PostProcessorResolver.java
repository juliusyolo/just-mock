package com.sdefaa.just.mock.common.task;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class PostProcessorResolver {

  public final static PostProcessorResolver INSTANCE = new PostProcessorResolver();

  private PostProcessorResolver(){

  }
  public AbstractPostProcessor resolve(String taskDefinition){
      return null;
  }
}
