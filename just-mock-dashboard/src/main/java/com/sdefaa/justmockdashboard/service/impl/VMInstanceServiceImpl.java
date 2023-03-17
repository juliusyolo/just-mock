package com.sdefaa.justmockdashboard.service.impl;

import com.sdefaa.justmockdashboard.bo.VMInstanceBO;
import com.sdefaa.justmockdashboard.enums.ResultStatus;
import com.sdefaa.justmockdashboard.exception.GlobalException;
import com.sdefaa.justmockdashboard.mapper.VMInstanceAttachMapper;
import com.sdefaa.justmockdashboard.mapper.VMInstanceMockInfoMapper;
import com.sdefaa.justmockdashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.justmockdashboard.pojo.model.VMInstanceAttachModel;
import com.sdefaa.justmockdashboard.pojo.model.VMInstanceMockInfoModel;
import com.sdefaa.justmockdashboard.service.VMInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机实例业务层实现
 * <p>
 * @since 1.0.0
 */
@Service
public class VMInstanceServiceImpl implements VMInstanceService {
  @Autowired
  private VMInstanceAttachMapper vmInstanceAttachMapper;
  @Autowired
  private VMInstanceMockInfoMapper vmInstanceMockInfoMapper;

  @Autowired
  private VMInstanceBO vmInstanceBO;

  @Override
  public List<VMInstanceDTO> getAllVMInstances() {
    List<VMInstanceAttachModel> vmInstanceAttachModelList = vmInstanceAttachMapper.selectVMInstanceAttachModelList();
    return vmInstanceBO.vmInstanceWrap(vmInstanceAttachModelList);
  }

  @Override
  public VMInstanceDTO attachVMInstance(String pid) {
    return vmInstanceBO.attachVMInstance(pid);
  }

  @Override
  @Transactional(rollbackFor = GlobalException.class)
  public void detachVMInstance(String pid) {
      try {
        vmInstanceAttachMapper.deleteVMInstanceAttachModelByPid(pid);
        vmInstanceMockInfoMapper.deleteVMInstanceMockInfoModelByPid(pid);
      }catch (Exception e){
        throw new GlobalException(ResultStatus.VM_DETACH_EXCEPTION);
      }
  }

}
