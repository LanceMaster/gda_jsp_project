package controller;

import java.io.IOException;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.dao.UserDAO;
import model.dto.UserDTO;

@WebServlet(urlPatterns = { "/user/*" }, initParams = { @WebInitParam(name = "view", value = "/view/") })
public class userController extends MskimRequestMapping {

	public UserDAO userDAO = new UserDAO();

	// 로그인폼 불러오기
	@RequestMapping("loginform")
	public String loginform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 로그인 폼을 보여주는 JSP 페이지로 이동
		return "user/loginform"; // JSP 페이지 경로
	}

	// 로그인 처리
	@RequestMapping("login")
	public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String password = request.getParameter("password");

		UserDTO userDTO = userDAO.login(id, password);

		if (userDTO != null) { // 로그인 성공
			request.getSession().setAttribute("user", userDTO);
			// 세션에 사용자 정보를 저장
			return "redirect:" + request.getContextPath() + "/user/mainpage";

		}

		else {
			request.setAttribute("loginError", "아이디 또는 비밀번호를 확인해주세요");
			return "user/loginform";
		}

	}

	// 로그아웃 처리
	@RequestMapping("logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().invalidate(); // 세션 무효화
		return "redirect:" + request.getContextPath() + "/user/mainpage"; // 로그인 폼으로 리다이렉트
	}

}
