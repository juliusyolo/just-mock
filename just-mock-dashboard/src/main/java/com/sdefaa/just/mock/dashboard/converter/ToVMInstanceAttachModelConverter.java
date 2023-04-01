package com.sdefaa.just.mock.dashboard.converter;

import com.sdefaa.just.mock.dashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceAttachInfoModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface ToVMInstanceAttachModelConverter {
    ToVMInstanceAttachModelConverter INSTANCE = Mappers.getMapper(ToVMInstanceAttachModelConverter.class);

    VMInstanceAttachInfoModel covert(VMInstanceDTO vmInstanceDTO);
}
