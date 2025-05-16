package model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import model.dto.UserDTO;
import model.mapper.AdminMapper;
import utils.MybatisConnection;

public class AdminDAO {

	private Class<AdminMapper> mapperClass = AdminMapper.class;

	// 전체 사용자 목록 조회
	public List<UserDTO> userList() {
		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(mapperClass).userList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

}
