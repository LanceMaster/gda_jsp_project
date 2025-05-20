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

    public List<ProjectsDTO> listAll() {
        return mapper.listAllWithLeaderName();
    }

    public ProjectsDTO findProjectWithLeaderName(int projectId) {
        return mapper.findProjectWithLeaderName(projectId);
    }

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

    // ✅ 제목 기반 검색 - 최신순
    public List<ProjectsDTO> searchByTitleOrderByRecent(String keyword) {
        return mapper.searchByTitleOrderByRecent(keyword);
    }
}
