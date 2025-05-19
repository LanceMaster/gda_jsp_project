package controller;

import model.dao.CartDAO;
import model.dto.LectureDTO;
import model.dto.UserDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/cart/view")
public class CartViewController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        // 로그인 확인
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/user/loginform");
            return;
        }

        int userId = user.getUserId();

        try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession()) {
            CartDAO cartDAO = new CartDAO(sqlSession);
            List<LectureDTO> cartLectures = cartDAO.getCartLectures(userId);
            req.setAttribute("cartLectures", cartLectures);
        }

        req.getRequestDispatcher("/view/lecture/cart.jsp").forward(req, resp);
    }
}
