package com.sdefaa.justmockdashboard.service;

import com.sdefaa.just.mock.common.pojo.ApiRegistryDTO;
import com.sdefaa.justmockdashboard.pojo.dto.VMInstanceDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机实例业务层
 * <p>
 * @since 1.0.0
 */
public interface VMInstanceService {
   List<VMInstanceDTO> getAllVMInstances();

  VMInstanceDTO attachVMInstance(String pid);

  void detachVMInstance(String pid);

  void registerApiList(ApiRegistryDTO apiRegistryDTO);
}
