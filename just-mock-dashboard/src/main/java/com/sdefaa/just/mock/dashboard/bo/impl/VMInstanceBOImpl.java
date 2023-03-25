package com.sdefaa.just.mock.dashboard.bo.impl;

import com.sdefaa.just.mock.dashboard.bo.VMInstanceBO;
import com.sdefaa.just.mock.dashboard.converter.ToVMInstanceDTOConverter;
import com.sdefaa.just.mock.dashboard.enums.ResultStatus;
import com.sdefaa.just.mock.dashboard.exception.GlobalException;
import com.sdefaa.just.mock.dashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceAttachModel;
import com.sun.tools.attach.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Component
public class VMInstanceBOImpl implements VMInstanceBO {

  private final static String VM_SELF = "com.sdefaa.just.mock.dashboard.JustMockDashboardApplication";
  @Value("${mock.agent.path}")
  private String mockAgentPath;
  @Value("${mock.config.path}")
  private String mockConfigPath;
  @Override
  public List<VMInstanceDTO> vmInstanceWrap(List<VMInstanceAttachModel> vmInstanceAttachModelList) {
    List<VirtualMachineDescriptor> virtualMachineDescriptors = VirtualMachine.list();
    // 所有的本地虚拟机实例
    List<VMInstanceDTO> allVMInstanceDTOs = virtualMachineDescriptors.stream().filter(virtualMachineDescriptor -> !Objects.equals(virtualMachineDescriptor.displayName(),VM_SELF)).map(virtualMachineDescriptor -> {
      VMInstanceDTO vmInstanceDTO = new VMInstanceDTO();
      if (virtualMachineDescriptor.displayName().isEmpty()){
        vmInstanceDTO.setName("无");
      }else {
        vmInstanceDTO.setName(virtualMachineDescriptor.displayName().split(" ")[0]);
      }
      vmInstanceDTO.setPid(virtualMachineDescriptor.id());
      vmInstanceDTO.setVendor(virtualMachineDescriptor.provider().name());
      vmInstanceDTO.setPlatform(virtualMachineDescriptor.provider().type());
      vmInstanceDTO.setAttached(false);
      return vmInstanceDTO;
    }).toList();
    // 数据库里存在连接的虚拟机实例
    List<VMInstanceDTO> attachedVMInstanceDTOs = vmInstanceAttachModelList.stream().map(ToVMInstanceDTOConverter.INSTANCE::covert).toList();
    // 未被连接的虚拟机实例
    List<VMInstanceDTO> unattachedVMInstanceDTOs = allVMInstanceDTOs.stream()
      .filter(vmInstanceDTO -> attachedVMInstanceDTOs.stream().noneMatch(attachedVMInstanceDTO -> Objects.equals(vmInstanceDTO.getPid(), attachedVMInstanceDTO.getPid())))
      .toList();
    // 过滤后的已连接的虚拟机实例
    List<VMInstanceDTO> filteredAttachedVMInstanceDTOs = allVMInstanceDTOs.stream()
      .filter(vmInstanceDTO -> attachedVMInstanceDTOs.stream().anyMatch(attachedVMInstanceDTO -> Objects.equals(vmInstanceDTO.getPid(), attachedVMInstanceDTO.getPid())))
      .peek(vmInstanceDTO -> vmInstanceDTO.setAttached(true))
      .toList();
    List<VMInstanceDTO> vmInstanceDTOList = new ArrayList<>();
    vmInstanceDTOList.addAll(unattachedVMInstanceDTOs);
    vmInstanceDTOList.addAll(filteredAttachedVMInstanceDTOs);
    vmInstanceDTOList.sort(Comparator.comparing(VMInstanceDTO::getPid));
    return vmInstanceDTOList;
  }

  @Override
  public VMInstanceDTO attachVMInstance(String pid) {
    List<VirtualMachineDescriptor> virtualMachineDescriptors = VirtualMachine.list();
    Optional<VirtualMachineDescriptor> vmDescriptor = virtualMachineDescriptors.stream().filter(virtualMachineDescriptor -> Objects.equals(virtualMachineDescriptor.id(), pid)).findFirst();
    if (vmDescriptor.isEmpty()) {
      throw new GlobalException(ResultStatus.VM_NOT_EXISTED);
    }
    VirtualMachineDescriptor virtualMachineDescriptor = vmDescriptor.get();
    try {
      VirtualMachine vm = VirtualMachine.attach(virtualMachineDescriptor);
      vm.loadAgent(mockAgentPath,mockConfigPath.concat(" ").concat(pid));
      vm.detach();
    } catch (AttachNotSupportedException e) {
      throw new GlobalException(ResultStatus.ATTACH_NOT_SUPPORTED_EXCEPTION, e);
    } catch (AgentLoadException e) {
      throw new GlobalException(ResultStatus.AGENT_LOAD_EXCEPTION, e);
    } catch (AgentInitializationException e) {
      throw new GlobalException(ResultStatus.AGENT_INITIALIZATION_EXCEPTION, e);
    }catch (IOException e){
      throw new GlobalException(ResultStatus.IO_EXCEPTION, e);
    }
    VMInstanceDTO vmInstanceDTO = new VMInstanceDTO();
    vmInstanceDTO.setName(virtualMachineDescriptor.displayName());
    vmInstanceDTO.setPid(virtualMachineDescriptor.id());
    vmInstanceDTO.setVendor(virtualMachineDescriptor.provider().name());
    vmInstanceDTO.setPlatform(virtualMachineDescriptor.provider().type());
    vmInstanceDTO.setAttached(true);
    return vmInstanceDTO;
  }
}