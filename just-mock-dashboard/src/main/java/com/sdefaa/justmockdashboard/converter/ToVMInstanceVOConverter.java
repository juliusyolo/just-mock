package com.sdefaa.justmockdashboard.converter;

import com.sdefaa.justmockdashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.justmockdashboard.pojo.vo.VMInstanceVO;
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
