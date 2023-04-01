package com.sdefaa.just.mock.dashboard.converter;

import com.sdefaa.just.mock.dashboard.pojo.dto.RegisteredApiInfoDTO;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceMockInfoModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface ToRegisteredApiInfoDTOConverter {
    ToRegisteredApiInfoDTOConverter INSTANCE = Mappers.getMapper(ToRegisteredApiInfoDTOConverter.class);

    RegisteredApiInfoDTO covert(VMInstanceMockInfoModel vmInstanceMockInfoModel);
}
