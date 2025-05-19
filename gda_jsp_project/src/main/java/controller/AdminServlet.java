package controller;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.dao.AdminDAO;
import model.dao.UserDAO;
import model.dto.UserDTO;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// DAO 객체 생성 (프로젝트에 맞게 조정)
	private AdminDAO adminDAO = new AdminDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 요청 인코딩 설정 (필요시)
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain; charset=UTF-8");

		String action = request.getParameter("action");

		// 디버깅용 파라미터 출력
		System.out.println("=== Request Parameters ===");
		request.getParameterMap().forEach((key, value) -> {
			System.out.println(key + ": " + String.join(", ", value));
		});
		System.out.println("=========================");

		if ("getUserDetail".equals(action)) {
			handleGetUserDetail(request, response);
			return;
		}

		if ("deleteUser".equals(action)) {
			handleDeleteUser(request, response);
			return;
		}

		// 다른 action 처리 시 추가...
	}

	private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String userId = request.getParameter("userId");
		System.out.println("userId: " + userId);
		UserDTO user = adminDAO.getUserById(userId);
		System.out.println("user: " + user);
		if (user != null) {
			boolean deleted = adminDAO.deleteUser(userId);
			if (deleted) {
				response.setStatus(HttpServletResponse.SC_OK);
				try {
					response.getWriter().write("User deleted successfully");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				try {
					response.getWriter().write("Failed to delete user");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.getWriter().write("User not found");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void handleGetUserDetail(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String userId = request.getParameter("userId");
		System.out.println("userId: " + userId);
		UserDTO user = adminDAO.getUserById(userId);
		System.out.println("user: " + user);
		if (user != null) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String json = gson.toJson(user);
			response.setContentType("application/json; charset=UTF-8");
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.getWriter().write("User not found");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
