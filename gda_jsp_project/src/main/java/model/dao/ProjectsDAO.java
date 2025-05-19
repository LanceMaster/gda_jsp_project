package model.dao;

import org.apache.ibatis.session.SqlSession;
import model.dto.ProjectsDTO;
import model.mapper.ProjectsMapper;
import java.util.List;

public class ProjectsDAO {
    private ProjectsMapper mapper;

    public ProjectsDAO(SqlSession session) {
        this.mapper = session.getMapper(ProjectsMapper.class);
    }

    // ✅ 리더 이름 포함 목록 조회
    public List<ProjectsDTO> listAll() {
        return mapper.listAllWithLeaderName();
    }

    // ✅ 리더 이름 포함 상세 조회
    public ProjectsDTO findProjectWithLeaderName(int projectId) {
        return mapper.findProjectWithLeaderName(projectId);
    }

    
    // ✅ 조회수 증가
    public void incrementViewCount(int projectId) {
        mapper.incrementViewCount(projectId);
    }
    
    public int insert(ProjectsDTO project) {
        return mapper.insert(project);
    }

    public int update(ProjectsDTO project) {
        return mapper.update(project);
    }

    public int delete(int projectId) {
        return mapper.delete(projectId);
    }
    
    public List<ProjectsDTO> listOrderByRecent() {
        return mapper.listOrderByRecent();
    }

    public List<ProjectsDTO> listOrderByViews() {
        return mapper.listOrderByViews();
    }

    public List<ProjectsDTO> listByStatusOrderByRecent(String status) {
        return mapper.listByStatusOrderByRecent(status);
    }

    public List<ProjectsDTO> listByStatusOrderByViews(String status) {
        return mapper.listByStatusOrderByViews(status);
    }

}
