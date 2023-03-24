package com.sdefaa.just.mock.dashboard.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sdefaa.just.mock.dashboard.bo.VMInstanceBO;
import com.sdefaa.just.mock.dashboard.converter.ToRegisteredApiInfoDTOConverter;
import com.sdefaa.just.mock.dashboard.converter.ToVMInstanceAttachModelConverter;
import com.sdefaa.just.mock.dashboard.converter.ToVMInstanceMockInfoModelConverter;
import com.sdefaa.just.mock.dashboard.enums.ResultStatus;
import com.sdefaa.just.mock.dashboard.exception.GlobalException;
import com.sdefaa.just.mock.dashboard.mapper.VMInstanceAttachMapper;
import com.sdefaa.just.mock.dashboard.mapper.VMInstanceMockInfoMapper;
import com.sdefaa.just.mock.dashboard.pojo.dto.RegisteredApiInfoDTO;
import com.sdefaa.just.mock.dashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceAttachModel;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceMockInfoModel;
import com.sdefaa.just.mock.dashboard.task.VMInstancePingTask;
import com.sdefaa.just.mock.common.pojo.ApiRegistryDTO;
import com.sdefaa.just.mock.dashboard.service.VMInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

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
  private ObjectMapper objectMapper;
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
  public List<RegisteredApiInfoDTO> getRegisteredApiList(String pid) {
    List<VMInstanceMockInfoModel> vmInstanceMockInfoModelList = vmInstanceMockInfoMapper.selectVMInstanceMockInfoModelList(pid);
    return vmInstanceMockInfoModelList.stream().map(ToRegisteredApiInfoDTOConverter.INSTANCE::covert).collect(Collectors.toList());
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
    List<VMInstanceMockInfoModel> vmInstanceMockInfoModelList = apiRegistryDTO.getApiInfos().stream().map(apiInfo -> {
      VMInstanceMockInfoModel vmInstanceMockInfoModel = ToVMInstanceMockInfoModelConverter.INSTANCE.covert(apiInfo);
      vmInstanceMockInfoModel.setPid(apiRegistryDTO.getPid());
      vmInstanceMockInfoModel.setClassName(apiInfo.getApiClassInfo().getClassName());
      vmInstanceMockInfoModel.setMethodName(apiInfo.getApiMethodInfo().getMethodName());
      try {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        vmInstanceMockInfoModel.setMethodArgsDesc(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiInfo.getApiMethodInfo().getInputs()));
        vmInstanceMockInfoModel.setMethodReturnDesc(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiInfo.getApiMethodInfo().getOutput()));
      } catch (JsonProcessingException e) {
        throw new GlobalException(ResultStatus.JSON_PROCESS_EXCEPTION, e);
      }
      return vmInstanceMockInfoModel;
    }).collect(Collectors.toList());
    int effectRows = vmInstanceMockInfoMapper.bulkInsertVMInstanceMockInfoModelList(vmInstanceMockInfoModelList);
    if (effectRows != apiRegistryDTO.getApiInfos().size()) {
      throw new GlobalException(ResultStatus.REGISTER_API_FAILED);
    }
    threadPoolTaskExecutor.submit(new VMInstancePingTask(restTemplate, this, apiRegistryDTO.getPid(), apiRegistryDTO.getPort()));
  }

}
