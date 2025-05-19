package controller;

import model.dao.CartDAO;
import model.dto.LectureDTO;
import model.dto.UserDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * 🛒 장바구니 조회 및 추가 통합 컨트롤러
 * GET  → /lecture/cart       → 장바구니 목록 조회
 * POST → /lecture/cart       → 장바구니에 강의 추가
 */
@WebServlet(
    urlPatterns = { "/lecture/cart" },
    initParams = {
        @WebInitParam(name = "view", value = "/view/")
    }
)
public class CartViewController extends HttpServlet {

    /**
     * 장바구니 조회 (GET)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null) {
            System.out.println("🚫 [CartController-GET] 로그인 세션 없음 → 리다이렉트");
            response.sendRedirect(request.getContextPath() + "/user/loginform");
            return;
        }

        int userId = user.getUserId();
        System.out.println("✅ [CartController-GET] 로그인 사용자 ID: " + userId);

        try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession()) {
            CartDAO cartDAO = new CartDAO(sqlSession);
            List<LectureDTO> cartLectures = cartDAO.getCartLectures(userId);

            System.out.println("📦 [CartController-GET] 장바구니 강의 수: " +
                    (cartLectures != null ? cartLectures.size() : 0));

            request.setAttribute("cartLectures", cartLectures);
            String viewPrefix = getServletConfig().getInitParameter("view");
            request.getRequestDispatcher(viewPrefix + "lecture/cart.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("❌ [CartController-GET] 장바구니 조회 중 예외 발생");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    /**
     * 장바구니 추가 (POST)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null) {
            System.out.println("🚫 [CartController-POST] 로그인 필요 → 로그인 폼 이동");
            response.sendRedirect(request.getContextPath() + "/user/loginform");
            return;
        }

        int userId = user.getUserId();
        int lectureId = Integer.parseInt(request.getParameter("lectureId"));

        try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            CartDAO cartDAO = new CartDAO(sqlSession);

            if (!cartDAO.isLectureInCart(userId, lectureId)) {
                cartDAO.addToCart(userId, lectureId);
                System.out.println("✅ [CartController-POST] 장바구니에 강의 추가 완료: " + lectureId);
            } else {
                System.out.println("ℹ️ [CartController-POST] 이미 장바구니에 있음: " + lectureId);
            }

            response.sendRedirect(request.getContextPath() + "/lecture/cart");

        } catch (Exception e) {
            System.err.println("❌ [CartController-POST] 장바구니 추가 중 오류 발생");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
