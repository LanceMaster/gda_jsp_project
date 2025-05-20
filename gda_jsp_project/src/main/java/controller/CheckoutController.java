package controller;

import model.dao.CheckoutDAO;
import model.dto.UserDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = { "/cart/checkout" }, initParams = { @WebInitParam(name = "view", value = "/view/") })
public class CheckoutController extends HttpServlet {

	public CheckoutDAO checkoutDAO = new CheckoutDAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();

		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user == null) {
			resp.sendRedirect(req.getContextPath() + "/user/loginform");
			return;
		}

		String[] lectureIds = req.getParameterValues("lectureIds");
		String[] amounts = req.getParameterValues("amounts");

		if (lectureIds == null || amounts == null || lectureIds.length != amounts.length) {
			resp.sendRedirect(req.getContextPath() + "/view/lecture/cart.jsp?error=선택된 강의 또는 금액 정보가 올바르지 않습니다");
			return;
		}

		try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
			for (int i = 0; i < lectureIds.length; i++) {
				int lectureId = Integer.parseInt(lectureIds[i]);
				int amountPaid = Integer.parseInt(amounts[i]);

				Map<String, Object> enrollmentParam = new HashMap<>();
				enrollmentParam.put("userId", user.getUserId());
				enrollmentParam.put("lectureId", lectureId);
				enrollmentParam.put("amountPaid", amountPaid);
				enrollmentParam.put("paymentMethod", "CARD");

				boolean success = checkoutDAO.insertEnrollment(sqlSession, enrollmentParam);

				if (!success) {
					sqlSession.rollback();
					resp.sendRedirect(req.getContextPath() + "/view/lecture/cart.jsp?error=결제 처리 중 오류가 발생했습니다");
					return;
				}

				Map<String, Object> cartParam = new HashMap<>();

				cartParam.put("userId", user.getUserId());
				cartParam.put("lectureId", lectureId);

				boolean deleteSuccess = checkoutDAO.deleteCartItem(sqlSession, cartParam);

				if (!deleteSuccess) {
					sqlSession.rollback();
					resp.sendRedirect(req.getContextPath() + "/view/lecture/cart.jsp?error=장바구니에서 강의 삭제 중 오류가 발생했습니다");
					return;
				}
			}

			resp.sendRedirect(req.getContextPath() + "/user/mypage");
		}

	}
}
