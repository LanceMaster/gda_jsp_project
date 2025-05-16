package model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import model.dto.ProjectsDTO;

@Mapper
public interface ProjectsMapper {

    // ✅ 전체 목록 조회 - 리더 이름 포함
    @Select("""
        SELECT p.*, u.name AS leaderName
        FROM projects p
        JOIN users u ON p.leader_id = u.user_id
    """)
    List<ProjectsDTO> listAllWithLeaderName();

    // ✅ 프로젝트 상세 조회 - 리더 이름 포함
    @Select("""
        SELECT p.*, u.name AS leaderName
        FROM projects p
        JOIN users u ON p.leader_id = u.user_id
        WHERE p.project_id = #{projectId}
    """)
    ProjectsDTO findProjectWithLeaderName(@Param("projectId") int projectId);

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
