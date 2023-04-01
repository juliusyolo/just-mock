package com.sdefaa.just.mock.dashboard.converter;

import com.sdefaa.just.mock.dashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceAttachInfoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Julius Wong
 * @date 2023/3/17 10:18
 * @since 1.0.0
 */
@Mapper
public interface ToVMInstanceDTOConverter {
    ToVMInstanceDTOConverter INSTANCE = Mappers.getMapper(ToVMInstanceDTOConverter.class);

    @Mapping(constant = "true", target = "attached")
    VMInstanceDTO covert(VMInstanceAttachInfoModel vmInstanceAttachInfoModel);

}
