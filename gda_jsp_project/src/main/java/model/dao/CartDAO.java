package model.dao;

import model.dto.LectureDTO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public class CartDAO {
    private final SqlSession session;

    public CartDAO(SqlSession session) {
        this.session = session;
    }

    public void addToCart(int userId, int lectureId) {
        session.insert("CartMapper.insertCart", Map.of("userId", userId, "lectureId", lectureId));
    }

    public List<LectureDTO> getCartLectures(int userId) {
        return session.selectList("CartMapper.selectCartLectures", userId);
    }

    public void deleteCart(int userId, int lectureId) {
        session.delete("CartMapper.deleteCartItem", Map.of("userId", userId, "lectureId", lectureId));
    }
}
