package model.dao;

import org.apache.ibatis.session.SqlSession;

import model.dto.UserDTO;
import model.mapper.MainMapper;
import utils.MybatisConnection;

public class UserDAO {
	private Class<MainMapper> mapperClass = MainMapper.class;

	public UserDTO login(String id, String pw) {

		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(mapperClass).login(id, pw);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;

	}

	public int emailDupCheck(String email) {

		SqlSession session = MybatisConnection.getConnection();
		try {
			// 이메일 중복 체크

			int resultCount = session.getMapper(mapperClass).emailDupCheck(email);
			if (resultCount > 0) {
				return 1; // 중복된 이메일
			} else {
				return 0; // 사용 가능한 이메일

			}

		} catch (Exception e) {

		}
		return 0;

	}

}
