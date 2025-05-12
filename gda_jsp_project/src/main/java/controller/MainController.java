package controller;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import dto.UserDTO;
import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;

@WebServlet(urlPatterns = { "/main/*" }, initParams = { @WebInitParam(name = "view", value = "/view/") })
public class MainController extends MskimRequestMapping {

	public UserDAO userDAO = new UserDAO();

	@RequestMapping("login")
	public String login(HttpServletRequest request, HttpServletResponse response) {

		String id = request.getParameter("username");
		String pw = request.getParameter("password");
		UserDTO userDTO = userDAO.login(id, pw);
		// 만약에 로그인 실패화면 데이터가 없을경우
		if (userDTO == null) {
			// 로그인 실패
			System.out.println("로그인 실패");
			// 로그인 실패시 다시 메인페이지이동
			return "main/mainpage";

		}
		// 로그인 성공시
		System.out.println("로그인 성공");
		return "redirect:/main/mainpage";

//		return "main/login";
	}

}
