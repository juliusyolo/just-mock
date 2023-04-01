package com.sdefaa.just.mock.dashboard.service;

import com.sdefaa.just.mock.common.pojo.ApiRegistryDTO;
import com.sdefaa.just.mock.dashboard.pojo.dto.*;

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

    List<RegisteredApiInfoDTO> getRegisteredApiList(String pid);

    VMInstanceDTO attachVMInstance(AttachVMInstanceDTO attachVMInstanceDTO);

    void detachVMInstance(String pid);

    void registerApiList(ApiRegistryDTO apiRegistryDTO);

    void removeMock(RemoveMockDTO removeMockDTO);

    void putMock(PutMockDTO putMockDTO);

}
