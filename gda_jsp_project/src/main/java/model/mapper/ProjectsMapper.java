package model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import model.dto.ProjectsDTO;

@Mapper
public interface ProjectsMapper {
    @Select("SELECT * FROM projects")
    List<ProjectsDTO> listAll();

    @Select("SELECT * FROM projects WHERE project_id = #{projectId}")
    ProjectsDTO listById(int projectId);

    @Insert("INSERT INTO projects (title, description, thumbnail, recruit_status, view_count, created_at, leader_id) "
          + "VALUES (#{title}, #{description}, #{thumbnail}, #{recruitStatus}, #{viewCount}, #{createdAt}, #{leaderId})")
    int insert(ProjectsDTO project);

    @Update("UPDATE projects SET title = #{title}, description = #{description}, thumbnail = #{thumbnail}, "
          + "recruit_status = #{recruitStatus}, view_count = #{viewCount}, created_at = #{createdAt}, leader_id = #{leaderId} "
          + "WHERE project_id = #{projectId}")
    int update(ProjectsDTO project);

    @Delete("DELETE FROM projects WHERE project_id = #{projectId}")
    int delete(int projectId);
}
