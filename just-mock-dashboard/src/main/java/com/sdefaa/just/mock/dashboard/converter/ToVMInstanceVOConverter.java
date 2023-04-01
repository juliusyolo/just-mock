package com.sdefaa.just.mock.dashboard.converter;

import com.sdefaa.just.mock.dashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.just.mock.dashboard.pojo.vo.VMInstanceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Julius Wong
 * @date 2023/3/17 12:36
 * @since 1.0.0
 */
@Mapper
public interface ToVMInstanceVOConverter {
    ToVMInstanceVOConverter INSTANCE = Mappers.getMapper(ToVMInstanceVOConverter.class);

    VMInstanceVO covert(VMInstanceDTO vmInstanceAttachModel);
}
