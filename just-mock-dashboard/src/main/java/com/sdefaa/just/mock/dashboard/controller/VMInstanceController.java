package com.sdefaa.just.mock.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.pojo.ApiRegistryDTO;
import com.sdefaa.just.mock.dashboard.converter.ToRegisteredApiInfoVOConverter;
import com.sdefaa.just.mock.dashboard.converter.ToVMInstanceVOConverter;
import com.sdefaa.just.mock.dashboard.enums.ResultStatus;
import com.sdefaa.just.mock.dashboard.pojo.ResponseWrapper;
import com.sdefaa.just.mock.dashboard.pojo.dto.AttachVMInstanceDTO;
import com.sdefaa.just.mock.dashboard.pojo.dto.PutMockDTO;
import com.sdefaa.just.mock.dashboard.pojo.dto.RemoveMockDTO;
import com.sdefaa.just.mock.dashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.just.mock.dashboard.pojo.vo.RegisteredApiInfoVO;
import com.sdefaa.just.mock.dashboard.pojo.vo.VMInstanceVO;
import com.sdefaa.just.mock.dashboard.service.VMInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机实例控制层
 * <p>
 * @since 1.0.0
 */
@RestController
@Slf4j
public class VMInstanceController {
    private final VMInstanceService vmInstanceService;
    @Autowired
    ObjectMapper objectMapper;

    public VMInstanceController(VMInstanceService vmInstanceService) {
        this.vmInstanceService = vmInstanceService;
    }

    @GetMapping(value = "/v1/api/vm/instances/list")
    public ResponseWrapper<List<VMInstanceVO>> getAllVMInstances() {
        List<VMInstanceDTO> vmInstanceDTOList = vmInstanceService.getAllVMInstances();
        List<VMInstanceVO> vmInstanceVOs = vmInstanceDTOList.stream().map(ToVMInstanceVOConverter.INSTANCE::covert).collect(Collectors.toList());
        return ResponseWrapper.wrap(ResultStatus.SUCCESS, vmInstanceVOs);
    }

    @PostMapping("/v1/api/vm/instance/attach")
    public ResponseWrapper<VMInstanceVO> attachVMInstance(@RequestBody AttachVMInstanceDTO attachVMInstanceDTO) {
        VMInstanceDTO vmInstanceDTO = vmInstanceService.attachVMInstance(attachVMInstanceDTO);
        VMInstanceVO vmInstanceVO = ToVMInstanceVOConverter.INSTANCE.covert(vmInstanceDTO);
        return ResponseWrapper.wrap(ResultStatus.SUCCESS, vmInstanceVO);
    }

    @GetMapping("/v1/api/vm/instance/{pid}/detach")
    public ResponseWrapper<Void> detachVMInstance(@PathVariable("pid") String pid) {
        vmInstanceService.detachVMInstance(pid);
        return ResponseWrapper.wrap(ResultStatus.SUCCESS);
    }

    @GetMapping("/v1/api/vm/instance/{pid}/attach/status")
    public ResponseWrapper<VMInstanceVO> getVMInstanceAttachedStatus(@PathVariable("pid") String pid) {
        return ResponseWrapper.wrap(ResultStatus.SUCCESS, new VMInstanceVO());
    }

    @PostMapping("/v1/api/vm/instance/api/register")
    public ResponseWrapper<Void> registerApiList(@RequestBody ApiRegistryDTO apiRegistryDTO) throws JsonProcessingException {
        log.info("register api:{}", objectMapper.writeValueAsString(apiRegistryDTO));
        vmInstanceService.registerApiList(apiRegistryDTO);
        return ResponseWrapper.wrap(ResultStatus.SUCCESS);
    }

    @GetMapping("/v1/api/vm/instance/{pid}/api/list")
    public ResponseWrapper<List<RegisteredApiInfoVO>> getRegisteredApiList(@PathVariable("pid") String pid) {
        List<RegisteredApiInfoVO> registeredApiInfoVOS = vmInstanceService.getRegisteredApiList(pid).stream().map(ToRegisteredApiInfoVOConverter.INSTANCE::covert).collect(Collectors.toList());
        return ResponseWrapper.wrap(ResultStatus.SUCCESS, registeredApiInfoVOS);
    }

    @PostMapping("/v1/api/vm/instance/mock/remove")
    public ResponseWrapper<Void> removeMock(@RequestBody RemoveMockDTO removeMockDTO) {
        vmInstanceService.removeMock(removeMockDTO);
        return ResponseWrapper.wrap(ResultStatus.SUCCESS);
    }

    @PostMapping("/v1/api/vm/instance/mock/put")
    public ResponseWrapper<Void> putMock(@RequestBody PutMockDTO putMockDTO) {
        vmInstanceService.putMock(putMockDTO);
        return ResponseWrapper.wrap(ResultStatus.SUCCESS);
    }
}
