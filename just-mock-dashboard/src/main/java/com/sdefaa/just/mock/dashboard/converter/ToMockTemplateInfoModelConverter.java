package com.sdefaa.just.mock.dashboard.converter;

import com.sdefaa.just.mock.dashboard.pojo.dto.MockTemplateInfoDTO;
import com.sdefaa.just.mock.dashboard.pojo.model.MockTemplateInfoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface ToMockTemplateInfoModelConverter {
  ToMockTemplateInfoModelConverter INSTANCE = Mappers.getMapper(ToMockTemplateInfoModelConverter.class);
  @Mapping(target = "randomVariables",ignore = true)
  @Mapping(target = "taskDefinitions",ignore = true)
  MockTemplateInfoModel covert(MockTemplateInfoDTO mockTemplateInfoDTO);
}
