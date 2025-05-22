package model.dao;

import org.apache.ibatis.session.SqlSession;

import model.dto.NaverUserDTO;
import utils.MybatisConnection;
import model.mapper.NaverMapper;

public class NaverUserDAO {
	private Class<NaverMapper> mapperClass = NaverMapper.class;

	public static NaverUserDTO findBySocialId(String socialId, String socialType) {
		// TODO Auto-generated method stub
		// 데이터베이스에서 socialId로 사용자 정보를 조회하는 로직을 구현합니다.

		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(NaverMapper.class).findBySocialId(socialId, socialType);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public static int insertNewUser(NaverUserDTO newUser) {
		// TODO Auto-generated method stub
		// 데이터베이스에 신규 사용자를 등록하는 로직을 구현합니다.
		SqlSession session = MybatisConnection.getConnection();
		try {
			NaverMapper mapper = session.getMapper(NaverMapper.class);
			int resultCount = mapper.insertNewUser(newUser);
			if (resultCount > 0) {
				session.commit();
				return 1; // 신규 사용자 등록 성공
			} else {
				session.rollback();
				return 0; // 신규 사용자 등록 실패
			}
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return 0;
	}

	public static NaverUserDTO getInformationByEmail(String email) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			NaverMapper mapper = session.getMapper(NaverMapper.class);
			NaverUserDTO naverUserDTO = mapper.getInformationByEmail(email);
			if (naverUserDTO.getSocialId() != null) {
				return naverUserDTO;
			} else {
				return null;
			}
		} catch (Exception e) {

			// TODO: handle exception
		} finally {
			session.close();
		}

		return null;
	}

}
