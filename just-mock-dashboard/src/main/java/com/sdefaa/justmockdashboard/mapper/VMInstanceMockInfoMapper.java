package com.sdefaa.justmockdashboard.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface VMInstanceMockInfoMapper {

  @Delete("delete from vm_instance_mock_info where pid = #{pid}")
  int deleteVMInstanceMockInfoModelByPid(@Param("pid")String pid);

}
