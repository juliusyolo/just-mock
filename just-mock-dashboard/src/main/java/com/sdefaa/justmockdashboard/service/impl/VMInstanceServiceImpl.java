package com.sdefaa.justmockdashboard.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.pojo.ApiRegistryDTO;
import com.sdefaa.justmockdashboard.bo.VMInstanceBO;
import com.sdefaa.justmockdashboard.converter.ToVMInstanceAttachModelConverter;
import com.sdefaa.justmockdashboard.enums.ResultStatus;
import com.sdefaa.justmockdashboard.exception.GlobalException;
import com.sdefaa.justmockdashboard.mapper.VMInstanceAttachMapper;
import com.sdefaa.justmockdashboard.mapper.VMInstanceMockInfoMapper;
import com.sdefaa.justmockdashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.justmockdashboard.pojo.model.VMInstanceAttachModel;
import com.sdefaa.justmockdashboard.service.VMInstanceService;
import com.sdefaa.justmockdashboard.task.VMInstancePingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机实例业务层实现
 * <p>
 * @since 1.0.0
 */
@Service
public class VMInstanceServiceImpl implements VMInstanceService {
  @Autowired
  private VMInstanceAttachMapper vmInstanceAttachMapper;
  @Autowired
  private VMInstanceMockInfoMapper vmInstanceMockInfoMapper;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  private VMInstanceBO vmInstanceBO;
  @Autowired
  private ThreadPoolTaskExecutor threadPoolTaskExecutor;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public List<VMInstanceDTO> getAllVMInstances() {
    List<VMInstanceAttachModel> vmInstanceAttachModelList = vmInstanceAttachMapper.selectVMInstanceAttachModelList();
    return vmInstanceBO.vmInstanceWrap(vmInstanceAttachModelList);
  }

  @Override
  public VMInstanceDTO attachVMInstance(String pid) {
    VMInstanceDTO vmInstanceDTO = vmInstanceBO.attachVMInstance(pid);
    VMInstanceAttachModel vmInstanceAttachModel = ToVMInstanceAttachModelConverter.INSTANCE.covert(vmInstanceDTO);
    int effectRows;
    try {
      effectRows = vmInstanceAttachMapper.insertVMInstanceAttachModel(vmInstanceAttachModel);
    } catch (Exception e) {
      throw new GlobalException(ResultStatus.VM_INSERT_EXCEPTION, e);
    }
    if (effectRows != 1) {
      throw new GlobalException(ResultStatus.VM_INSERT_FAILED);
    }
    return vmInstanceDTO;
  }

  @Override
  @Transactional(rollbackFor = GlobalException.class)
  public void detachVMInstance(String pid) {
    try {
      vmInstanceAttachMapper.deleteVMInstanceAttachModelByPid(pid);
      vmInstanceMockInfoMapper.deleteVMInstanceMockInfoModelByPid(pid);
    } catch (Exception e) {
      throw new GlobalException(ResultStatus.VM_DETACH_EXCEPTION);
    }
  }

  @Override
  public void registerApiList(ApiRegistryDTO apiRegistryDTO) {
    threadPoolTaskExecutor.submit(new VMInstancePingTask(restTemplate, this,apiRegistryDTO.getPid(), apiRegistryDTO.getPort()));
  }

}
