package model.dao;

import org.apache.ibatis.session.SqlSession;
import model.dto.ProjectsDTO;
import model.mapper.ProjectsMapper;
import java.util.List;

public class ProjectsDAO {
    private ProjectsMapper mapper;

    // 외부에서 SqlSession을 받아서 사용
    public ProjectsDAO(SqlSession session) {
        this.mapper = session.getMapper(ProjectsMapper.class);
    }

    // 전체 목록 조회
    public List<ProjectsDTO> listAll() {
        return mapper.listAll();
    }

    // 특정 프로젝트 조회
    public ProjectsDTO listById(int projectId) {
        return mapper.listById(projectId);
    }

    // 프로젝트 등록
    public int insert(ProjectsDTO project) {
        return mapper.insert(project);
    }

    // 프로젝트 수정
    public int update(ProjectsDTO project) {
        return mapper.update(project);
    }

    // 프로젝트 삭제
    public int delete(int projectId) {
        return mapper.delete(projectId);
    }
}