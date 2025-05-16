package model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;
import model.dto.ProjectsDTO;

@Mapper
public interface ProjectsMapper {

    @Select("SELECT * FROM projects")
    List<ProjectsDTO> listAll();

    @Select("SELECT * FROM projects WHERE project_id = #{projectId}")
    ProjectsDTO listById(int projectId);

    @Insert("INSERT INTO projects (title, description, recruit_status, view_count, leader_id) "
    	      + "VALUES (#{title}, #{description}, #{recruitStatus}, #{viewCount}, #{leaderId})")
    	@Options(useGeneratedKeys = true, keyProperty = "projectId")
    	int insert(ProjectsDTO project);

    @Update("UPDATE projects SET title = #{title}, description = #{description}, "
    	      + "recruit_status = #{recruitStatus}, view_count = #{viewCount}, leader_id = #{leaderId} "
    	      + "WHERE project_id = #{projectId}")
    	int update(ProjectsDTO project);

    @Delete("DELETE FROM projects WHERE project_id = #{projectId}")
    int delete(int projectId);
}
