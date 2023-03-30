package com.sdefaa.just.mock.common.task;

import com.sdefaa.just.mock.common.strategy.AbstractMockStrategy;

import java.util.logging.Logger;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public abstract class AbstractPostProcessor implements Runnable {
  private static final Logger logger = Logger.getLogger(AbstractPostProcessor.class.getName());

  protected abstract void process();

  @Override
  public void run() {
    try {
      Thread.sleep(2000);
      this.process();
    } catch (Exception e) {
      logger.info("Mock后置处理发生异常," + e);
    }

  }
}
