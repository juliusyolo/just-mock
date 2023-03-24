package com.sdefaa.just.mock.dashboard.converter;

import com.sdefaa.just.mock.common.pojo.ApiInfo;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceMockInfoModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface ToVMInstanceMockInfoModelConverter {

  ToVMInstanceMockInfoModelConverter INSTANCE = Mappers.getMapper(ToVMInstanceMockInfoModelConverter.class);

  VMInstanceMockInfoModel covert(ApiInfo apiInfo);

}
