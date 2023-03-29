package com.sdefaa.just.mock.dashboard.mapper;

import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceMockInfoModel;
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
    "insert into vm_instance_mock_info(pid,class_name,class_annotations_desc,method_annotations_desc,method_name,method_args_desc,method_return_desc,api_url,api_type,api_method,mock_enable) values" +
    "<foreach collection='vmInstanceMockInfoModelList' item='vmInstanceMockInfoModel' separator=','>" +
    "(#{vmInstanceMockInfoModel.pid},#{vmInstanceMockInfoModel.className},#{vmInstanceMockInfoModel.classAnnotationsDesc},#{vmInstanceMockInfoModel.methodAnnotationsDesc},#{vmInstanceMockInfoModel.methodName},#{vmInstanceMockInfoModel.methodArgsDesc},#{vmInstanceMockInfoModel.methodReturnDesc},#{vmInstanceMockInfoModel.apiUrl},#{vmInstanceMockInfoModel.apiType},#{vmInstanceMockInfoModel.apiMethod},#{vmInstanceMockInfoModel.mockEnable})" +
    "</foreach>" +
    "</script>")
  int bulkInsertVMInstanceMockInfoModelList(@Param("vmInstanceMockInfoModelList") List<VMInstanceMockInfoModel> vmInstanceMockInfoModelList);

  @Select("select * from vm_instance_mock_info where pid = #{pid}")
  List<VMInstanceMockInfoModel> selectVMInstanceMockInfoModelList(@Param("pid")String pid);

  @Update("update vm_instance_mock_info set mock_enable = 0, mock_template_id = null  where pid = #{pid} and class_name = #{className} and method_name=#{methodName}")
  int updateVMInstanceMockInfoModelByPid(@Param("pid")String pid,@Param("className")String className,@Param("methodName")String methodName);

  @Update("update vm_instance_mock_info set mock_enable = #{mockEnable}, mock_template_id = #{mockTemplateId} where pid = #{pid} and class_name = #{className} and method_name=#{methodName}")
  int updateVMInstanceMockInfoModel(VMInstanceMockInfoModel  vmInstanceMockInfoModel);

}
