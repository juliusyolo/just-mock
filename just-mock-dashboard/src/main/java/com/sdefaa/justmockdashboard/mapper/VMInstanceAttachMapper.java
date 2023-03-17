package com.sdefaa.justmockdashboard.mapper;

import com.sdefaa.justmockdashboard.pojo.model.VMInstanceAttachModel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface  VMInstanceAttachMapper {

  @Select("select * from vm_instance_attach")
  List<VMInstanceAttachModel> selectVMInstanceAttachModelList();

  @Delete("delete from vm_instance_attach where pid = #{pid}")
  int deleteVMInstanceAttachModelByPid(@Param("pid")String pid);


}
