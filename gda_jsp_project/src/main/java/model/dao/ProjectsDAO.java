package model.dao;

import org.apache.ibatis.session.SqlSession;
import java.util.List;
import model.dto.ProjectsDTO;
import model.mapper.ProjectsMapper;
import utils.MybatisConnection;

public class ProjectsDAO {
    private SqlSession session;
    private ProjectsMapper mapper;

    public ProjectsDAO() {
        this.session = MybatisConnection.getConnection();
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

    // 세션 종료
    public void close() {
        MybatisConnection.close(session);
    }
}
