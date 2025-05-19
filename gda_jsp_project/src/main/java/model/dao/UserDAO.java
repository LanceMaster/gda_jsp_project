package model.dao;

import java.util.List;

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

	public int signup(UserDTO userDTO) {
		// TODO Auto-generated method stub
		SqlSession session = MybatisConnection.getConnection();
		try {
			int resultCount = session.getMapper(mapperClass).signup(userDTO);
			if (resultCount > 0) {
				session.commit();
				return 1; // 회원가입 성공
			} else {
				session.rollback();
				return 0; // 회원가입 실패
			}
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return 0; // 회원가입 실패

	}

	/**
	 * 비밀번호 변경 메서드
	 * 
	 * @param email
	 * @param newPassword
	 * @return
	 */
	public boolean updatePasswordByEmail(String email, String newPassword) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			int resultCount = session.getMapper(mapperClass).updatePasswordByEmail(email, newPassword);

			if (resultCount > 0) {
				session.commit();
				return true; // 비밀번호 변경 성공
			} else {
				session.rollback();
				return false; // 비밀번호 변경 실패
			}
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return false;
	}

	public boolean deleteAccount(String email) {
		// TODO Auto-generated method stub
		SqlSession session = MybatisConnection.getConnection();
		try {
			int resultCount = session.getMapper(mapperClass).deleteAccount(email);
			if (resultCount > 0) {
				session.commit();
				return true; // 계정 삭제 성공
			} else {
				session.rollback();
				return false; // 계정 삭제 실패
			}
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return false;
	}

	/**
	 * 아이디 찾기 메서드
	 * 
	 * @param name
	 * @param email
	 * @return
	 */
	public String findId(String name, String email) {
		// TODO Auto-generated method stub
		SqlSession session = MybatisConnection.getConnection();
		try {
			String id = session.getMapper(mapperClass).findId(name, email);
			if (id != null) {
				return id; // 아이디 찾기 성공
			} else {
				return null; // 아이디 찾기 실패
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public boolean findPassword(String email, String name) {
		// TODO Auto-generated method stub

		SqlSession session = MybatisConnection.getConnection();
		try {
			int resultCount = session.getMapper(mapperClass).findPassword(email, name);
			if (resultCount > 0) {
				return true; // 비밀번호 찾기 성공
			} else {
				return false; // 비밀번호 찾기 실패
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return false;

	}

	public String findIdByNameAndBirth(String name, String birth) {
		// TODO Auto-generated method stub
		SqlSession session = MybatisConnection.getConnection();
		try {
			String id = session.getMapper(mapperClass).findId(name, birth);
			if (id != null) {
				return id; // 아이디 찾기 성공
			} else {
				return null; // 아이디 찾기 실패
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
	}

	/**
	 * 이력서 파일명 가져오기 메서드
	 * 
	 * @param parameter
	 * @return
	 */
	public String getResumeFilename(String email) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			String resumeFilename = session.getMapper(mapperClass).getResumeFilename(email);
			if (resumeFilename != null) {
				return resumeFilename; // 이력서 파일명 가져오기 성공
			} else {
				return null; // 이력서 파일명 가져오기 실패
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
	}

}
