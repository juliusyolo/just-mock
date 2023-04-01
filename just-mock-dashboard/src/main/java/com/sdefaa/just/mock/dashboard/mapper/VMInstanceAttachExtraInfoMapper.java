package com.sdefaa.just.mock.dashboard.mapper;

import com.sdefaa.just.mock.dashboard.pojo.model.VMInstanceAttachExtraInfoModel;
import org.apache.ibatis.annotations.*;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface VMInstanceAttachExtraInfoMapper {
    @Insert("insert into vm_instance_attach_extra_info(pid,port) values(#{pid},#{port})")
    int insertVMInstanceAttachExtraInfoModel(VMInstanceAttachExtraInfoModel vmInstanceAttachExtraInfoModel);

    @Delete("delete from vm_instance_attach_extra_info where pid = #{pid}")
    int deleteVMInstanceAttachExtraInfoModelByPid(@Param("pid") String pid);

    @Select("select * from vm_instance_attach_extra_info where pid = #{pid}")
    VMInstanceAttachExtraInfoModel selectVMInstanceAttachExtraInfoModelByPid(@Param("pid") String pid);
}
