package com.sdefaa.just.mock.agent;

import com.sdefaa.just.mock.common.pojo.ApiInfo;
import com.sdefaa.just.mock.common.utils.CommonUtils;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class MockAgentMain {
  public static void main(String[] args) {
    System.out.println("Hello world!");
  }
  private static final AtomicBoolean LOADED = new AtomicBoolean(false);

  private static final List<Class> LOADED_TARGET_CLASSES = new ArrayList<>();

  public static final Map<String,Object> map = new ConcurrentHashMap<>();
  public static void agentmain(String[] args, Instrumentation instrumentation) throws UnmodifiableClassException {
    if (LOADED.compareAndSet(false,true)){
      Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
      List<Class> loadedTargetClasses = CommonUtils.generateTargetClass(allLoadedClasses).collect(Collectors.toList());
      LOADED_TARGET_CLASSES.addAll(loadedTargetClasses);
      List<ApiInfo> apiInfoList = loadedTargetClasses.stream()
        .flatMap(CommonUtils::generateApiInfoFromTargetClass)
        .toList();
      // TODO  upload apiInfoList

      // 启动
      ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
      executorService.submit(()->{

      });

      // 添加虚拟机shutdownHook
      Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdownNow));
    }






  }

}
