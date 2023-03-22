package com.sdefaa.justmockdashboard.mapper;

import com.sdefaa.justmockdashboard.pojo.model.VMInstanceAttachModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface  VMInstanceAttachMapper {

  @Select("select * from vm_instance_attach")
  List<VMInstanceAttachModel> selectVMInstanceAttachModelList();

  @Insert("insert into vm_instance_attach(pid,name,platform,vendor) values(#{pid},#{name},#{platform},#{vendor})")
  int insertVMInstanceAttachModel(VMInstanceAttachModel model);

  @Delete("delete from vm_instance_attach where pid = #{pid}")
  int deleteVMInstanceAttachModelByPid(@Param("pid")String pid);


}
