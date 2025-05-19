package model.dao;

import model.dto.LectureDTO;
import model.mapper.CartMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CartDAO {
    private final CartMapper cartMapper;

    public CartDAO(SqlSession session) {
        this.cartMapper = session.getMapper(CartMapper.class); // ✅ 어노테이션 기반 매퍼 주입
    }

    // ✅ 장바구니에 강의 추가
    public void addToCart(int userId, int lectureId) {
        cartMapper.insertCart(userId, lectureId);
    }

    // ✅ 중복 체크 (장바구니에 해당 강의가 있는지 확인)
    public boolean isLectureInCart(int userId, int lectureId) {
        return cartMapper.existsInCart(userId, lectureId) > 0;
    }

    // ✅ 사용자의 장바구니 강의 목록 조회
    public List<LectureDTO> getCartLectures(int userId) {
        return cartMapper.selectCartLectures(userId);
    }

    // ✅ 장바구니에서 특정 강의 삭제
    public void deleteCart(int userId, int lectureId) {
        cartMapper.deleteCartItem(userId, lectureId);
    }
}
