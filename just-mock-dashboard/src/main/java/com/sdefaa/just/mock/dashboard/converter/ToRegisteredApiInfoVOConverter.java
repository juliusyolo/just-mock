package com.sdefaa.just.mock.dashboard.converter;

import com.sdefaa.just.mock.dashboard.pojo.dto.RegisteredApiInfoDTO;
import com.sdefaa.just.mock.dashboard.pojo.vo.RegisteredApiInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface ToRegisteredApiInfoVOConverter {
  ToRegisteredApiInfoVOConverter INSTANCE = Mappers.getMapper(ToRegisteredApiInfoVOConverter.class);

  RegisteredApiInfoVO covert(RegisteredApiInfoDTO registeredApiInfoDTO);
}
