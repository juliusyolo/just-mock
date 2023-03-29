package com.sdefaa.just.mock.dashboard.mapper;

import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceAttachModel;
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

  @Insert("insert into vm_instance_attach(pid) values(#{pid})")
  int insertVMInstanceAttachModel(VMInstanceAttachModel model);


  @Delete("delete from vm_instance_attach where pid = #{pid}")
  int deleteVMInstanceAttachModelByPid(@Param("pid")String pid);

  @Update("<script>update vm_instance_attach " +
    "<set>" +
    "<if test='name!=null'>name = #{name},</if>" +
    "<if test='platform!=null'>platform = #{platform},</if>" +
    "<if test='vendor!=null'>vendor = #{vendor},</if>" +
    "<if test='port!=null'>port = #{port},</if>" +
    "</set>"+
    "where pid = #{pid}" +
    "</script>")
  int updateVMInstanceAttachModel(VMInstanceAttachModel model);


  @Select("select * from vm_instance_attach where pid = #{pid}")
  VMInstanceAttachModel selectVMInstanceAttachModelByPid(@Param("pid")String pid);

}
