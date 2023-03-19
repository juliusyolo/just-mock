package com.sdefaa.justmockdashboard.controller;

import com.sdefaa.just.mock.common.pojo.ApiRegistryDTO;
import com.sdefaa.justmockdashboard.converter.ToVMInstanceVOConverter;
import com.sdefaa.justmockdashboard.enums.ResultStatus;
import com.sdefaa.justmockdashboard.pojo.ResponseWrapper;
import com.sdefaa.justmockdashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.justmockdashboard.pojo.vo.VMInstanceVO;
import com.sdefaa.justmockdashboard.service.VMInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public VMInstanceController(VMInstanceService vmInstanceService) {
        this.vmInstanceService = vmInstanceService;
    }

    @GetMapping(value = "/v1/api/vm/instances/list")
    public ResponseWrapper<List<VMInstanceVO>> getAllVMInstances() {
        List<VMInstanceDTO> vmInstanceDTOList = vmInstanceService.getAllVMInstances();
        List<VMInstanceVO> vmInstanceVOs = vmInstanceDTOList.stream().map(ToVMInstanceVOConverter.INSTANCE::covert).toList();
        return ResponseWrapper.wrap(ResultStatus.SUCCESS, vmInstanceVOs);
    }

    @GetMapping("/v1/api/vm/instance/{pid}/attach")
    public ResponseWrapper<VMInstanceVO> attachVMInstance(@PathVariable("pid") String pid) {
        VMInstanceDTO vmInstanceDTO = vmInstanceService.attachVMInstance(pid);
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
    public ResponseWrapper<Void> registerApiList(@RequestBody ApiRegistryDTO apiRegistryDTO) {
        log.info("register api:{}",apiRegistryDTO.toString());
        return ResponseWrapper.wrap(ResultStatus.SUCCESS);
    }

}
