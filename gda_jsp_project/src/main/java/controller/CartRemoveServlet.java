package controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.CartDAO;
import model.dao.LectureDAO;
import model.dao.UserDAO;
import model.dto.LectureDTO;
import model.dto.UserDTO;

@WebServlet(urlPatterns = { "/cart/remove" }, initParams = { @WebInitParam(name = "view", value = "/view/") })
public class CartRemoveServlet extends HttpServlet {

	private CartDAO cartDAO = new CartDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
//        List<LectureDTO> cartLectures = (List<LectureDTO>) session.getAttribute("cartLectures");

		String lectureIdParam = request.getParameter("lectureId");
		UserDTO user = (UserDTO) session.getAttribute("user");

		if (lectureIdParam != null) {
			try {
				int lectureId = Integer.parseInt(lectureIdParam);

				boolean deleteSuccess = cartDAO.deleteCart(lectureId, user.getUserId());

				if (deleteSuccess) {

					response.setStatus(HttpServletResponse.SC_OK);
					
				} else {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "삭제 실패");

				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
