package com.sdefaa.justmockdashboard.task;

import com.sdefaa.just.mock.common.constant.CommonConstant;
import com.sdefaa.justmockdashboard.service.VMInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机探活任务
 * <p>
 * @since 1.0.0
 */
@Slf4j
public class VMInstancePingTask implements Runnable {

    private final RestTemplate restTemplate;

    private final VMInstanceService vmInstanceService;

    private final String pid;
    private final int port;

  public VMInstancePingTask(RestTemplate restTemplate, VMInstanceService vmInstanceService, String pid, int port) {
    this.restTemplate = restTemplate;
    this.vmInstanceService = vmInstanceService;
    this.pid = pid;
    this.port = port;
  }

  private volatile boolean continued = true;

    private volatile int maxPingCount = 3;

    @Override
    public void run() {
        while (continued) {
            try {
                ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://127.0.0.1:" + this.port + "/mock/agent/ping", String.class);
                if (!Objects.equals(responseEntity.getBody(), CommonConstant.PONG)) {
                    maxPingCount--;
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                maxPingCount--;
            }
            if (maxPingCount == 0) {
              continued = false;
              log.info("retry 3 times for ping {},should detached!",this.pid);
              vmInstanceService.detachVMInstance(this.pid);
            }
        }
    }
}
