package com.sdefaa.just.mock.dashboard.service;

import com.sdefaa.just.mock.dashboard.pojo.dto.MockTemplateInfoDTO;
import com.sdefaa.just.mock.dashboard.pojo.vo.MockTemplateInfoVO;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public interface MockTemplateService {

  List<MockTemplateInfoDTO> getMockTemplateInfoList();

  void removeMockTemplateInfo(Long id);

  void putMockTemplateInfo(MockTemplateInfoDTO mockTemplateInfoDTO);

}
