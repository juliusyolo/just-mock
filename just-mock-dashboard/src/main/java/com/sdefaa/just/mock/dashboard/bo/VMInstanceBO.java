package com.sdefaa.just.mock.dashboard.bo;

import com.sdefaa.just.mock.dashboard.pojo.dto.VMInstanceDTO;
import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceAttachModel;

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
