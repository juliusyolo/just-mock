package com.sdefaa.just.mock.dashboard.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.pojo.RandomVariable;
import com.sdefaa.just.mock.dashboard.converter.ToMockTemplateInfoDTOConverter;
import com.sdefaa.just.mock.dashboard.converter.ToMockTemplateInfoModelConverter;
import com.sdefaa.just.mock.dashboard.enums.ResultStatus;
import com.sdefaa.just.mock.dashboard.exception.GlobalException;
import com.sdefaa.just.mock.dashboard.mapper.MockTemplateInfoMapper;
import com.sdefaa.just.mock.dashboard.pojo.dto.MockTemplateInfoDTO;
import com.sdefaa.just.mock.dashboard.pojo.model.MockTemplateInfoModel;
import com.sdefaa.just.mock.dashboard.service.MockTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Service
public class MockTemplateServiceImpl implements MockTemplateService {

    @Autowired
    private MockTemplateInfoMapper mockTemplateInfoMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<MockTemplateInfoDTO> getMockTemplateInfoList() {
        List<MockTemplateInfoModel> mockTemplateInfoModels = mockTemplateInfoMapper.selectMockTemplateInfoModelList();
        return mockTemplateInfoModels.stream().map(mockTemplateInfoModel -> {
            MockTemplateInfoDTO mockTemplateInfoDTO = ToMockTemplateInfoDTOConverter.INSTANCE.covert(mockTemplateInfoModel);
            try {
                mockTemplateInfoDTO.setRandomVariables(objectMapper.readValue(mockTemplateInfoModel.getRandomVariables(), new TypeReference<List<RandomVariable>>() {
                }));
                mockTemplateInfoDTO.setTaskDefinitions(objectMapper.readValue(mockTemplateInfoModel.getTaskDefinitions(), new TypeReference<List<String>>() {
                }));
                return mockTemplateInfoDTO;
            } catch (Exception e) {
                throw new GlobalException(ResultStatus.QUERY_TEMPLATE_EXCEPTION, e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void removeMockTemplateInfo(Long id) {
        int effectRows;
        try {
            effectRows = mockTemplateInfoMapper.deleteMockTemplateInfoModel(id);
        } catch (Exception e) {
            throw new GlobalException(ResultStatus.REMOVE_MOCK_TEMPLATE_EXCEPTION, e);
        }
        if (effectRows != 1) {
            throw new GlobalException(ResultStatus.REMOVE_MOCK_TEMPLATE_EXCEPTION);
        }
    }

    @Override
    public void putMockTemplateInfo(MockTemplateInfoDTO mockTemplateInfoDTO) {
        int effectRows;
        MockTemplateInfoModel mockTemplateInfoModel = ToMockTemplateInfoModelConverter.INSTANCE.covert(mockTemplateInfoDTO);
        try {
            if (Objects.equals(mockTemplateInfoModel.getEl(), "")) {
                mockTemplateInfoModel.setEl(null);
            }
            mockTemplateInfoModel.setRandomVariables(objectMapper.writeValueAsString(mockTemplateInfoDTO.getRandomVariables()));
            mockTemplateInfoModel.setTaskDefinitions(objectMapper.writeValueAsString(mockTemplateInfoDTO.getTaskDefinitions()));
            if (Objects.isNull(mockTemplateInfoModel.getId())) {
                effectRows = mockTemplateInfoMapper.insertMockTemplateInfoModel(mockTemplateInfoModel);
            } else {
                effectRows = mockTemplateInfoMapper.updateMockTemplateInfoModel(mockTemplateInfoModel);
            }
        } catch (Exception e) {
            throw new GlobalException(ResultStatus.PUT_MOCK_TEMPLATE_EXCEPTION, e);
        }
        if (effectRows != 1) {
            throw new GlobalException(ResultStatus.PUT_MOCK_TEMPLATE_FAILED);
        }
    }

}
