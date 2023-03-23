package com.sdefaa.justmockdashboard.bo;

import com.sdefaa.justmockdashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.justmockdashboard.pojo.model.VMInstanceAttachModel;

import java.util.List;

/**
 * @author Julius Wong
 * <p>
 * 虚拟机实例业务实体
 * <p>
 * @since 1.0.0
 */
public interface VMInstanceBO {
  List<VMInstanceDTO> vmInstanceWrap(List<VMInstanceAttachModel> vmInstanceAttachModelList);

  VMInstanceDTO attachVMInstance(String pid);
}
