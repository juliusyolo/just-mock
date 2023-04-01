package com.sdefaa.just.mock.dashboard.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.constant.CommonConstant;
import com.sdefaa.just.mock.common.pojo.ApiMockCommandDTO;
import com.sdefaa.just.mock.common.pojo.ApiMockCommandResponseDTO;
import com.sdefaa.just.mock.common.pojo.ApiRegistryDTO;
import com.sdefaa.just.mock.dashboard.bo.VMInstanceBO;
import com.sdefaa.just.mock.dashboard.converter.ToRegisteredApiInfoDTOConverter;
import com.sdefaa.just.mock.dashboard.converter.ToVMInstanceAttachModelConverter;
import com.sdefaa.just.mock.dashboard.converter.ToVMInstanceMockInfoModelConverter;
import com.sdefaa.just.mock.dashboard.enums.ResultStatus;
import com.sdefaa.just.mock.dashboard.exception.GlobalException;
import com.sdefaa.just.mock.dashboard.mapper.VMInstanceAttachExtraInfoMapper;
import com.sdefaa.just.mock.dashboard.mapper.VMInstanceAttachInfoMapper;
import com.sdefaa.just.mock.dashboard.mapper.VMInstanceMockInfoMapper;
import com.sdefaa.just.mock.dashboard.pojo.dto.*;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceAttachExtraInfoModel;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceAttachInfoModel;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceMockInfoModel;
import com.sdefaa.just.mock.dashboard.service.VMInstanceService;
import com.sdefaa.just.mock.dashboard.task.VMInstancePingTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机实例业务层实现
 * <p>
 * @since 1.0.0
 */
@Service
@Slf4j
public class VMInstanceServiceImpl implements VMInstanceService {
    @Autowired
    private VMInstanceAttachInfoMapper vmInstanceAttachInfoMapper;
    @Autowired
    private VMInstanceMockInfoMapper vmInstanceMockInfoMapper;
    @Autowired
    private VMInstanceAttachExtraInfoMapper vmInstanceAttachExtraInfoMapper;
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
        List<VMInstanceAttachInfoModel> vmInstanceAttachInfoModelList = vmInstanceAttachInfoMapper.selectVMInstanceAttachModelList();
        return vmInstanceBO.vmInstanceWrap(vmInstanceAttachInfoModelList);
    }

    @Override
    public List<RegisteredApiInfoDTO> getRegisteredApiList(String pid) {
        List<VMInstanceMockInfoModel> vmInstanceMockInfoModelList = vmInstanceMockInfoMapper.selectVMInstanceMockInfoModelList(pid);
        return vmInstanceMockInfoModelList.stream().map(ToRegisteredApiInfoDTOConverter.INSTANCE::covert).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VMInstanceDTO attachVMInstance(AttachVMInstanceDTO attachVMInstanceDTO) {
        String environments = null;
        if (Objects.nonNull(attachVMInstanceDTO.getEnvironmentVariables()) && !attachVMInstanceDTO.getEnvironmentVariables().isEmpty()) {
            environments = attachVMInstanceDTO.getEnvironmentVariables().stream().collect(Collectors.joining(" "));
        }
        VMInstanceDTO vmInstanceDTO = vmInstanceBO.attachVMInstance(attachVMInstanceDTO.getPid(), environments);
        VMInstanceAttachInfoModel vmInstanceAttachInfoModel = ToVMInstanceAttachModelConverter.INSTANCE.covert(vmInstanceDTO);
        vmInstanceAttachInfoModel.setEnvironmentVariables(environments);

        int effectRows;
        try {
            effectRows = vmInstanceAttachInfoMapper.insertVMInstanceAttachModel(vmInstanceAttachInfoModel);
        } catch (Exception e) {
            throw new GlobalException(ResultStatus.VM_INSERT_EXCEPTION, e);
        }
        if (effectRows != 1) {
            throw new GlobalException(ResultStatus.VM_INSERT_FAILED);
        }
        return vmInstanceDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void detachVMInstance(String pid) {
        try {
            vmInstanceAttachInfoMapper.deleteVMInstanceAttachModelByPid(pid);
            vmInstanceMockInfoMapper.deleteVMInstanceMockInfoModelByPid(pid);
            vmInstanceAttachExtraInfoMapper.deleteVMInstanceAttachExtraInfoModelByPid(pid);
        } catch (Exception e) {
            throw new GlobalException(ResultStatus.VM_DETACH_EXCEPTION);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerApiList(ApiRegistryDTO apiRegistryDTO) {
        List<VMInstanceMockInfoModel> vmInstanceMockInfoModelList = apiRegistryDTO.getApiInfos().stream().map(apiInfo -> {
            VMInstanceMockInfoModel vmInstanceMockInfoModel = ToVMInstanceMockInfoModelConverter.INSTANCE.covert(apiInfo);
            vmInstanceMockInfoModel.setPid(apiRegistryDTO.getPid());
            vmInstanceMockInfoModel.setClassName(apiInfo.getApiClassInfo().getClassName());
            vmInstanceMockInfoModel.setMethodName(apiInfo.getApiMethodInfo().getMethodName());
            vmInstanceMockInfoModel.setMockEnable(false);
            try {
                vmInstanceMockInfoModel.setMethodArgsDesc(objectMapper.writeValueAsString(apiInfo.getApiMethodInfo().getInputs()));
                vmInstanceMockInfoModel.setMethodReturnDesc(objectMapper.writeValueAsString(apiInfo.getApiMethodInfo().getOutput()));
                vmInstanceMockInfoModel.setClassAnnotationsDesc(objectMapper.writeValueAsString(apiInfo.getApiClassInfo().getAnnotations()));
                vmInstanceMockInfoModel.setMethodAnnotationsDesc(objectMapper.writeValueAsString(apiInfo.getApiMethodInfo().getAnnotations()));
            } catch (JsonProcessingException e) {
                throw new GlobalException(ResultStatus.JSON_PROCESS_EXCEPTION, e);
            }
            return vmInstanceMockInfoModel;
        }).collect(Collectors.toList());
        if (!vmInstanceMockInfoModelList.isEmpty()) {
            int effectRows = vmInstanceMockInfoMapper.bulkInsertVMInstanceMockInfoModelList(vmInstanceMockInfoModelList);
            if (effectRows != apiRegistryDTO.getApiInfos().size()) {
                throw new GlobalException(ResultStatus.REGISTER_API_FAILED);
            }
        }
        VMInstanceAttachExtraInfoModel vmInstanceAttachExtraInfoModel = new VMInstanceAttachExtraInfoModel();
        vmInstanceAttachExtraInfoModel.setPid(apiRegistryDTO.getPid());
        vmInstanceAttachExtraInfoModel.setPort(apiRegistryDTO.getPort());
        int effectRows = vmInstanceAttachExtraInfoMapper.insertVMInstanceAttachExtraInfoModel(vmInstanceAttachExtraInfoModel);
        if (effectRows != 1) {
            throw new GlobalException(ResultStatus.REGISTER_API_FAILED);
        }
        threadPoolTaskExecutor.submit(new VMInstancePingTask(restTemplate, this, apiRegistryDTO.getPid(), apiRegistryDTO.getPort()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMock(RemoveMockDTO removeMockDTO) {
        VMInstanceMockInfoModel vmInstanceMockInfoModel = ToVMInstanceMockInfoModelConverter.INSTANCE.covert(removeMockDTO);
        vmInstanceMockInfoModel.setMockEnable(false);
        int updateEffectRows;
        try {
            updateEffectRows = vmInstanceMockInfoMapper.updateVMInstanceMockInfoModel(vmInstanceMockInfoModel);
        } catch (Exception e) {
            throw new GlobalException(ResultStatus.UPDATE_MOCK_ENABLE_FLAG_EXCEPTION, e);
        }
        if (updateEffectRows != 1) {
            throw new GlobalException(ResultStatus.UPDATE_MOCK_ENABLE_FLAG_FAILED);
        }
        VMInstanceAttachExtraInfoModel vmInstanceAttachExtraInfoModel;
        try {
            vmInstanceAttachExtraInfoModel = vmInstanceAttachExtraInfoMapper.selectVMInstanceAttachExtraInfoModelByPid(removeMockDTO.getPid());
        } catch (Exception e) {
            throw new GlobalException(ResultStatus.QUERY_MOCK_COMMAND_PORT_EXCEPTION, e);
        }
        ApiMockCommandDTO apiMockCommandDTO = new ApiMockCommandDTO();
        apiMockCommandDTO.setCommandType(ApiMockCommandDTO.CommandType.REMOVE.name());
        apiMockCommandDTO.setClazzName(removeMockDTO.getClassName());
        apiMockCommandDTO.setMethodName(removeMockDTO.getMethodName());
        ApiMockCommandResponseDTO apiMockCommandResponseDTO;
        try {
            apiMockCommandResponseDTO = restTemplate.postForObject("http://127.0.0.1:" + vmInstanceAttachExtraInfoModel.getPort() + CommonConstant.COMMAND_URL, apiMockCommandDTO, ApiMockCommandResponseDTO.class);
            log.info("MockCommand Response: {}", apiMockCommandResponseDTO);
        } catch (RestClientException e) {
            throw new GlobalException(ResultStatus.CALL_MOCK_COMMAND_EXCEPTION, e);
        }
        if (!Objects.equals(apiMockCommandResponseDTO.getCode(), CommonConstant.SUCCESS_CODE)) {
            throw new GlobalException(ResultStatus.CALL_MOCK_COMMAND_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void putMock(PutMockDTO putMockDTO) {
        VMInstanceMockInfoModel vmInstanceMockInfoModel = ToVMInstanceMockInfoModelConverter.INSTANCE.covert(putMockDTO);
        vmInstanceMockInfoModel.setMockEnable(true);
        int updateEffectRows;
        try {
            updateEffectRows = vmInstanceMockInfoMapper.updateVMInstanceMockInfoModel(vmInstanceMockInfoModel);
        } catch (Exception e) {
            throw new GlobalException(ResultStatus.UPDATE_MOCK_ENABLE_FLAG_EXCEPTION, e);
        }
        if (updateEffectRows != 1) {
            throw new GlobalException(ResultStatus.UPDATE_MOCK_ENABLE_FLAG_FAILED);
        }
        VMInstanceAttachExtraInfoModel vmInstanceAttachExtraInfoModel;
        try {
            vmInstanceAttachExtraInfoModel = vmInstanceAttachExtraInfoMapper.selectVMInstanceAttachExtraInfoModelByPid(putMockDTO.getPid());
        } catch (Exception e) {
            throw new GlobalException(ResultStatus.QUERY_MOCK_COMMAND_PORT_EXCEPTION, e);
        }
        ApiMockCommandDTO apiMockCommandDTO = new ApiMockCommandDTO();
        apiMockCommandDTO.setCommandType(ApiMockCommandDTO.CommandType.PUT.name());
        apiMockCommandDTO.setClazzName(putMockDTO.getClassName());
        apiMockCommandDTO.setMethodName(putMockDTO.getMethodName());
        apiMockCommandDTO.setEl(putMockDTO.getEl());
        apiMockCommandDTO.setTemplateContent(putMockDTO.getTemplateContent());
        apiMockCommandDTO.setRandomVariables(putMockDTO.getRandomVariables());
        apiMockCommandDTO.setTaskDefinitions(putMockDTO.getTaskDefinitions());
        ApiMockCommandResponseDTO apiMockCommandResponseDTO;
        try {
            apiMockCommandResponseDTO = restTemplate.postForObject("http://127.0.0.1:" + vmInstanceAttachExtraInfoModel.getPort() + CommonConstant.COMMAND_URL, apiMockCommandDTO, ApiMockCommandResponseDTO.class);
            log.info("MockCommand Response: {}", apiMockCommandResponseDTO);
        } catch (RestClientException e) {
            throw new GlobalException(ResultStatus.CALL_MOCK_COMMAND_EXCEPTION, e);
        }
        if (!Objects.equals(apiMockCommandResponseDTO.getCode(), CommonConstant.SUCCESS_CODE)) {
            throw new GlobalException(ResultStatus.CALL_MOCK_COMMAND_FAILED);
        }
    }

}
