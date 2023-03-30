package com.sdefaa.just.mock.dashboard.mapper;

import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceAttachInfoModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface VMInstanceAttachInfoMapper {

  @Select("select * from vm_instance_attach_info")
  List<VMInstanceAttachInfoModel> selectVMInstanceAttachModelList();

  @Insert("insert into vm_instance_attach_info(pid,name,platform,vendor,environment_variables) values(#{pid},#{name},#{platform},#{vendor},#{environmentVariables})")
  int insertVMInstanceAttachModel(VMInstanceAttachInfoModel model);


  @Delete("delete from vm_instance_attach_info where pid = #{pid}")
  int deleteVMInstanceAttachModelByPid(@Param("pid")String pid);


}
