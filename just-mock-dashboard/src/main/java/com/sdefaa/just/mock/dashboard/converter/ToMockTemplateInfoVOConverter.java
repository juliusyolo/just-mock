package com.sdefaa.just.mock.dashboard.converter;

import com.sdefaa.just.mock.dashboard.pojo.dto.MockTemplateInfoDTO;
import com.sdefaa.just.mock.dashboard.pojo.vo.MockTemplateInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface ToMockTemplateInfoVOConverter {
    ToMockTemplateInfoVOConverter INSTANCE = Mappers.getMapper(ToMockTemplateInfoVOConverter.class);

    MockTemplateInfoVO covert(MockTemplateInfoDTO mockTemplateInfoDTO);
}
