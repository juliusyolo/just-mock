package com.sdefaa.just.mock.dashboard.mapper;

import com.sdefaa.just.mock.dashboard.pojo.model.MockTemplateInfoModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Mapper
public interface MockTemplateInfoMapper {

  @Select("select * from mock_template_info")
  List<MockTemplateInfoModel> selectMockTemplateInfoModelList();

  @Delete("delete from mock_template_info where id = #{id}")
  int deleteMockTemplateInfoModel(@Param("id")Long id);

  @Insert("insert into mock_template_info(template_content,el,tag,random_variables,task_definitions) values(#{templateContent},#{el},#{tag},#{randomVariables},#{taskDefinitions})")
  int insertMockTemplateInfoModel(MockTemplateInfoModel mockTemplateInfoModel);

  @Update("<script>update mock_template_info " +
    "<set>" +
    "el = #{el}," +
    "<if test='templateContent!=null'>template_content = #{templateContent},</if>" +
    "<if test='tag!=null'>tag = #{tag},</if>" +
    "<if test='randomVariables!=null'>random_variables = #{randomVariables},</if>" +
    "<if test='taskDefinitions!=null'>task_definitions = #{taskDefinitions},</if>" +
    "</set>" +
    "where id = #{id}" +
    "</script>")
  int updateMockTemplateInfoModel(MockTemplateInfoModel mockTemplateInfoModel);

}
