package com.sdefaa.justmockdashboard.controller;

import com.sdefaa.justmockdashboard.pojo.VMInstanceVO;
import com.sdefaa.justmockdashboard.service.VMInstanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机实例控制层
 * <p>
 * @since 1.0.0
 */
@RestController
public class VMInstanceController {
    private final VMInstanceService vmInstanceService;

    public VMInstanceController(VMInstanceService vmInstanceService) {
        this.vmInstanceService = vmInstanceService;
    }

    @GetMapping("/v1/api/vm/instances/list")
    List<VMInstanceVO> getAllVMInstances(){
        return null;
    }
}
