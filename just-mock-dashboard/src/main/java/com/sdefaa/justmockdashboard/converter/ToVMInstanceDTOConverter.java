package com.sdefaa.justmockdashboard.converter;

import com.sdefaa.justmockdashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.justmockdashboard.pojo.model.VMInstanceAttachModel;
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

  @Mapping(constant = "true",target = "attached")
  VMInstanceDTO covert(VMInstanceAttachModel vmInstanceAttachModel);

}
