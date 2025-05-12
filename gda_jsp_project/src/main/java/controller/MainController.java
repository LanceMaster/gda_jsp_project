package controller;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import dto.UserDTO;
import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;

@WebServlet(urlPatterns = { "/main/*" }, 
initParams = { @WebInitParam(name = "view", value = "/view/") })
public class MainController extends MskimRequestMapping {
    
	public UserDAO userDAO = new UserDAO();
	@RequestMapping("login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		
		String id = request.getParameter("username");
		String pw = request.getParameter("password");
		UserDTO userDTO = userDAO.login(id, pw);

        System.out.println("아이디 : " + id);
        System.out.println("비밀번호 : " + pw);
		
//		return "main/login";
		return null;
	}
	
	
	

}
