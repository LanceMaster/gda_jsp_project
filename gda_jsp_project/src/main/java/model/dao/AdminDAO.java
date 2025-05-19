package model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import model.dto.UserDTO;
import model.mapper.AdminMapper;
import utils.MybatisConnection;

public class AdminDAO {

	private Class<AdminMapper> mapperClass = AdminMapper.class;

	// 수강생만 조회 일반 회원조회
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

	public UserDTO getUserById(String userId) {
		// TODO Auto-generated method stub

		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(mapperClass).getUserById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	public boolean deleteUser(String userId) {
		// TODO Auto-generated method stub
		SqlSession session = MybatisConnection.getConnection();
		try {
			int result = session.getMapper(mapperClass).deleteUser(userId);
			if (result > 0) {
				session.commit();
				return true;
			} else {
				session.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return false;
	}

	/**
	 * 사용자 이름으로 검색
	 * 
	 * @param keyword
	 * @return
	 */
	public List<UserDTO> searchUsersByName(String keyword) {
		// TODO Auto-generated method stub
		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(mapperClass).searchUsersByName(keyword);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

}
