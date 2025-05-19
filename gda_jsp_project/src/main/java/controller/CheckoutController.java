package controller;

import model.dto.UserDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("/user/loginform");
            return;
        }

        String[] lectureIds = req.getParameterValues("lectureIds");
        if (lectureIds == null || lectureIds.length == 0) {
            resp.sendRedirect("/view/lecture/cart.jsp?error=선택된 강의가 없습니다");
            return;
        }

        try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            for (String id : lectureIds) {
                int lectureId = Integer.parseInt(id);

                // 수강 등록 처리
                sqlSession.insert("EnrollmentMapper.insert", Map.of(
                    "userId", user.getUserId(),
                    "lectureId", lectureId,
                    "amountPaid", 0,
                    "paymentMethod", "CARD"
                ));

                // 장바구니에서 삭제
                sqlSession.delete("CartMapper.deleteCartItem", Map.of(
                    "userId", user.getUserId(),
                    "lectureId", lectureId
                ));
            }
        }

        resp.sendRedirect("/user/mypage");
    }
}
