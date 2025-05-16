package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.dao.AdminDAO;
import model.dao.UserDAO;
import model.dto.UserDTO;

@WebServlet("/admin/userdetail")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AdminDAO adminDAO = new AdminDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("userid");
		UserDTO user = adminDAO.getUserById(userId);

		if (user != null) {
			response.setContentType("application/json; charset=UTF-8");

			// 날짜 포맷 변환 (yyyy-MM-dd)
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String birthdateStr = user.getBirthdate() != null ? sdf.format(user.getBirthdate()) : "";

			// JSON 객체 생성
			UserJson userJson = new UserJson(user.getUserId(), user.getName(), user.getEmail(), birthdateStr,
					user.getPhone());

			String json = new Gson().toJson(userJson);
			response.getWriter().write(json);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "사용자를 찾을 수 없습니다.");
		}
	}

	// JSON용 DTO 클래스 (필요한 데이터만 전달)
	private static class UserJson {
		private int userId;
		private String name;
		private String email;
		private String birthdate;
		private String phone;

		public UserJson(int i, String name, String email, String birthdate, String phone) {
			this.userId = i;
			this.name = name;
			this.email = email;
			this.birthdate = birthdate;
			this.phone = phone;
		}
	}
}
