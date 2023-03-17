package com.sdefaa.justmockdashboard.service.impl;

import com.sdefaa.justmockdashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.justmockdashboard.service.VMInstanceService;
import org.springframework.stereotype.Service;

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

    @Override
    public List<VMInstanceDTO> getAllVMInstances() {
        return null;
    }
}
