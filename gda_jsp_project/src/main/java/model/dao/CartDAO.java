package model.dao;

import model.dto.LectureDTO;
import model.mapper.CartMapper;
import model.mapper.MainMapper;
import utils.MybatisConnection;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CartDAO {
	private Class<CartMapper> mapperClass = CartMapper.class;

	// ✅ 장바구니에 강의 추가
	public void addToCart(int userId, int lectureId) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			session.getMapper(mapperClass).insertCart(userId, lectureId);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ✅ 중복 체크 (장바구니에 해당 강의가 있는지 확인)
	public boolean isLectureInCart(int userId, int lectureId) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(mapperClass).existsInCart(userId, lectureId) > 0;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	// ✅ 사용자의 장바구니 강의 목록 조회
	public List<LectureDTO> getCartLectures(int userId) {
		SqlSession session = MybatisConnection.getConnection();

		try {
			return session.getMapper(mapperClass).selectCartLectures(userId);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public boolean deleteCart(int lectureId, int userId) {
		// TODO Auto-generated method stub
		SqlSession session = MybatisConnection.getConnection();

		try {
			boolean result =  session.getMapper(mapperClass).deleteCartItem(userId, lectureId) > 0;
			if(result) session.commit();
			return result;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			session.close();
		}
		return false;

	}

	/*
	 * // ✅ 장바구니에서 특정 강의 삭제 public static int deleteCart(int userId, int lectureId)
	 * { return cartm
	 * 
	 * }
	 */
}
