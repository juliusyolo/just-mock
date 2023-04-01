package com.sdefaa.just.mock.dashboard.controller;

import com.sdefaa.just.mock.dashboard.converter.ToMockTemplateInfoVOConverter;
import com.sdefaa.just.mock.dashboard.enums.ResultStatus;
import com.sdefaa.just.mock.dashboard.pojo.ResponseWrapper;
import com.sdefaa.just.mock.dashboard.pojo.dto.MockTemplateInfoDTO;
import com.sdefaa.just.mock.dashboard.pojo.vo.MockTemplateInfoVO;
import com.sdefaa.just.mock.dashboard.service.MockTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@RestController
public class MockTemplateController {

    @Autowired
    private MockTemplateService mockTemplateService;

    @GetMapping("/v1/api/mock/template/list")
    public ResponseWrapper<List<MockTemplateInfoVO>> getMockTemplateInfoList() {
        List<MockTemplateInfoVO> mockTemplateInfoVOS = mockTemplateService.getMockTemplateInfoList().stream().map(ToMockTemplateInfoVOConverter.INSTANCE::covert).collect(Collectors.toList());
        return ResponseWrapper.wrap(ResultStatus.SUCCESS, mockTemplateInfoVOS);
    }

    @GetMapping("/v1/api/mock/template/{id}/remove")
    public ResponseWrapper<Void> removeMockTemplateInfo(@PathVariable("id") Long id) {
        mockTemplateService.removeMockTemplateInfo(id);
        return ResponseWrapper.wrap(ResultStatus.SUCCESS);
    }

    @PostMapping("/v1/api/mock/template/put")
    public ResponseWrapper<Void> putMockTemplateInfo(@RequestBody MockTemplateInfoDTO mockTemplateInfoDTO) {
        mockTemplateService.putMockTemplateInfo(mockTemplateInfoDTO);
        return ResponseWrapper.wrap(ResultStatus.SUCCESS);
    }
}
