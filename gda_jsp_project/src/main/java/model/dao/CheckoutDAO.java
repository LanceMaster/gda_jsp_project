package model.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import model.mapper.CartMapper;
import model.mapper.EnrollmentMapper;

public class CheckoutDAO {

	public boolean insertEnrollment(SqlSession sqlSession, Map<String, Object> param) {
	    EnrollmentMapper mapper = sqlSession.getMapper(EnrollmentMapper.class);
	    int result = mapper.insertEnrollment(
	        (int) param.get("userId"),
	        (int) param.get("lectureId"),
	        (int) param.get("amountPaid"),
	        (String) param.get("paymentMethod")
	    );
	    return result > 0;
	}

	public boolean deleteCartItem(SqlSession sqlSession, Map<String, Object> cartParam) {
		// TODO Auto-generated method stub
		// 장바구니에서 강의 삭제
		// cartParam에는 lectureId와 userId가 포함되어 있어야 합니다.
		
		CartMapper mapper = sqlSession.getMapper(CartMapper.class);
		int result = mapper.deleteCartItem(
			(int) cartParam.get("userId"),
			(int) cartParam.get("lectureId")
		);
		
		return result > 0;
	}


}
