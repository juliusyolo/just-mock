package com.sdefaa.just.mock.common.constant;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Julius Wong
 * <p>
 * 公共常量
 * <p>
 * @since 1.0.0
 */
public class CommonConstant {
  public static final String PONG = "PONG";

  public static final String NOT_FOUND = "404";

  public static final String APPLICATION_JSON = "application/json";

  public static final String TEXT_PLAIN = "text/plain";

  public static final String PING_URL = "/mock/agent/ping";

  public static final String ACTIVE_URL = "/mock/agent/active";

  public static void main(String[] args) {
    List<VirtualMachineDescriptor> virtualMachineDescriptors = VirtualMachine.list();
    Optional<VirtualMachineDescriptor> vmDescriptor = virtualMachineDescriptors.stream().filter(virtualMachineDescriptor -> Objects.equals(virtualMachineDescriptor.displayName(), "com.sdefaa.justmockdashboard.JustMockDashboardApplication")).findFirst();
    if (vmDescriptor.isEmpty()) {
      throw new RuntimeException();
    }
    VirtualMachineDescriptor virtualMachineDescriptor = vmDescriptor.get();
    try {
      VirtualMachine vm = VirtualMachine.attach(virtualMachineDescriptor);
      //vm.loadAgent("E:\\Workspace\\IdeaProjects\\creative-explorer-all\\just-mock\\just-mock-agent\\target\\just-mock-agent-1.0.0-RELEASE.jar", "E:\\Workspace\\IdeaProjects\\creative-explorer-all\\just-mock\\just-mock-agent\\src\\main\\resources\\agent.yml " + virtualMachineDescriptor.id());
      vm.loadAgent("/Users/julius/WorkSpace/IdeaProjects/just-mock/just-mock-agent/target/just-mock-agent-1.0.0-RELEASE.jar", "/Users/julius/WorkSpace/IdeaProjects/just-mock/just-mock-agent/src/main/resources/agent.yml " + virtualMachineDescriptor.id());
      vm.detach();
    } catch (AttachNotSupportedException e) {
      throw new RuntimeException(e);
    } catch (AgentLoadException e) {
      throw new RuntimeException(e);
    } catch (AgentInitializationException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
