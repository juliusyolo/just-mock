package com.sdefaa.justmockdashboard.converter;

import com.sdefaa.justmockdashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.justmockdashboard.pojo.model.VMInstanceAttachModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface ToVMInstanceAttachModelConverter {
  ToVMInstanceAttachModelConverter INSTANCE = Mappers.getMapper(ToVMInstanceAttachModelConverter.class);
  VMInstanceAttachModel covert(VMInstanceDTO vmInstanceDTO);
}
