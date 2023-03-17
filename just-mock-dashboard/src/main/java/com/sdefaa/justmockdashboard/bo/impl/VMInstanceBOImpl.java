package com.sdefaa.justmockdashboard.bo.impl;

import com.sdefaa.justmockdashboard.bo.VMInstanceBO;
import com.sdefaa.justmockdashboard.converter.ToVMInstanceDTOConverter;
import com.sdefaa.justmockdashboard.enums.ResultStatus;
import com.sdefaa.justmockdashboard.exception.GlobalException;
import com.sdefaa.justmockdashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.justmockdashboard.pojo.model.VMInstanceAttachModel;
import com.sun.tools.attach.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Component
public class VMInstanceBOImpl implements VMInstanceBO {

  private final static String VM_SELF = "com.sdefaa.justmockdashboard.JustMockDashboardApplication";
  @Value("${mock.agent.path}")
  private String mockAgentPath;

  @Override
  public List<VMInstanceDTO> vmInstanceWrap(List<VMInstanceAttachModel> vmInstanceAttachModelList) {
    List<VirtualMachineDescriptor> virtualMachineDescriptors = VirtualMachine.list();
    // 所有的本地虚拟机实例
    List<VMInstanceDTO> allVMInstanceDTOs = virtualMachineDescriptors.stream().filter(virtualMachineDescriptor -> !Objects.equals(virtualMachineDescriptor.displayName(),VM_SELF)).map(virtualMachineDescriptor -> {
      VMInstanceDTO vmInstanceDTO = new VMInstanceDTO();
      vmInstanceDTO.setName(virtualMachineDescriptor.displayName());
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
      .filter(vmInstanceDTO -> attachedVMInstanceDTOs.stream().noneMatch(attachedVMInstanceDTO -> Objects.equals(vmInstanceDTO.getName(), attachedVMInstanceDTO.getName())))
      .toList();
    // 过滤后的已连接的虚拟机实例
    List<VMInstanceDTO> filteredAttachedVMInstanceDTOs = allVMInstanceDTOs.stream()
      .filter(vmInstanceDTO -> attachedVMInstanceDTOs.stream().anyMatch(attachedVMInstanceDTO -> Objects.equals(vmInstanceDTO.getName(), attachedVMInstanceDTO.getName())))
      .peek(vmInstanceDTO -> vmInstanceDTO.setAttached(true))
      .toList();
    List<VMInstanceDTO> vmInstanceDTOList = new ArrayList<>();
    vmInstanceDTOList.addAll(unattachedVMInstanceDTOs);
    vmInstanceDTOList.addAll(filteredAttachedVMInstanceDTOs);
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
      vm.loadAgent(mockAgentPath);
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
