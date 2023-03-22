package com.sdefaa.justmockdashboard.mapper;

import com.sdefaa.justmockdashboard.pojo.model.VMInstanceMockInfoModel;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface VMInstanceMockInfoMapper {

  @Delete("delete from vm_instance_mock_info where pid = #{pid}")
  int deleteVMInstanceMockInfoModelByPid(@Param("pid")String pid);

  @Insert("<script>" +
    "insert into vm_instance_mock_info(pid,class_name,method_name,method_arg_desc,method_return_desc,api_url,api_type) values" +
    "<foreach collection='vmInstanceMockInfoModelList' item='vmInstanceMockInfoModel' separator=','>" +
    "(#{vmInstanceMockInfoModel.pid},#{vmInstanceMockInfoModel.className},#{vmInstanceMockInfoModel.methodName},#{vmInstanceMockInfoModel.methodArgDesc},#{vmInstanceMockInfoModel.methodReturnDesc},#{vmInstanceMockInfoModel.apiUrl},#{vmInstanceMockInfoModel.apiType})" +
    "</foreach>" +
    "</script>")
  int bulkInsertVMInstanceMockInfoModelList(@Param("vmInstanceMockInfoModelList") List<VMInstanceMockInfoModel> vmInstanceMockInfoModelList);

  @Select("select * from vm_instance_mock_info where pid = #{pid}")
  List<VMInstanceMockInfoModel> selectVMInstanceMockInfoModelList(@Param("pid")String pid);
}
