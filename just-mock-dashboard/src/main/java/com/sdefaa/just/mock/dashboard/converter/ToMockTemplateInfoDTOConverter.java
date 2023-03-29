package com.sdefaa.just.mock.dashboard.converter;

import com.sdefaa.just.mock.dashboard.pojo.dto.MockTemplateInfoDTO;
import com.sdefaa.just.mock.dashboard.pojo.model.MockTemplateInfoModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface ToMockTemplateInfoDTOConverter {
  ToMockTemplateInfoDTOConverter INSTANCE = Mappers.getMapper(ToMockTemplateInfoDTOConverter.class);

  MockTemplateInfoDTO covert(MockTemplateInfoModel mockTemplateInfoModel);
}
