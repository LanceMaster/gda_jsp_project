package controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.dao.UserDAO;
import model.dto.UserDTO;

@WebServlet(urlPatterns = { "/user/*" }, initParams = { @WebInitParam(name = "view", value = "/view/") })
public class UserController extends MskimRequestMapping {

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

		if (userDTO != null) {
			// 로그인 성공
			request.getSession().setAttribute("user", userDTO);

			// 이전에 저장된 redirect URL 확인
			String redirectUrl = (String) request.getSession().getAttribute("redirectAfterLogin");
			if (redirectUrl != null) {
				request.getSession().removeAttribute("redirectAfterLogin");
				return "redirect:" + redirectUrl;
			}

			// 기본적으로는 메인페이지로
			return "redirect:" + request.getContextPath() + "/user/mainpage";
		} else {
			request.setAttribute("loginError", "아이디 또는 비밀번호를 확인해주세요");
			return "user/loginform";
		}
	}

	// 회원가입폼 불러오기
	@RequestMapping("signupform")
	public String signupform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 회원가입 폼을 보여주는 JSP 페이지로 이동
		return "user/signupform"; // JSP 페이지 경로
	}

	/**
	 * 회원가입 처리
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("signup")
	public String signup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 업로드 폴더 경로 설정 (예: webapp/upload/user/)
		String path = request.getServletContext().getRealPath("/") + "/upload/user/";
		File dir = new File(path);
		if (!dir.exists())
			dir.mkdirs();

		int maxSize = 10 * 1024 * 1024; // 10MB
		MultipartRequest multi = null;

		try {
			multi = new MultipartRequest(request, path, maxSize, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			request.setAttribute("signupError", "파일 업로드 실패");
			return "user/signupform"; // 실패 시 다시 폼으로
		}

		// MultipartRequest에서 파라미터 꺼내기
		String email = multi.getParameter("email");
		String password = multi.getParameter("password");
//		String profileFileName = multi.getFilesystemName("profile"); // 업로드된 파일명 (예: 프로필 이미지)

		// DTO에 데이터 세팅
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(email);
		userDTO.setPassword(password);
//	    userDTO.setProfileFilename(profileFileName != null ? profileFileName : "");

		int result = userDAO.signup(userDTO);

		if (result > 0) {
			return "redirect:" + request.getContextPath() + "/user/loginform"; // 성공시 로그인 폼으로
		} else {
			request.setAttribute("signupError", "회원가입 실패");
			return "user/signupform"; // 실패시 다시 폼으로
		}
	}

	// 아이디찾기폼 불러오기
	@RequestMapping("findidform")
	public String findidform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 아이디 찾기 폼을 보여주는 JSP 페이지로 이동
		return "user/findidform"; // JSP 페이지 경로
	}

	// 로그아웃 처리
	@RequestMapping("logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().invalidate(); // 세션 무효화
		return "redirect:" + request.getContextPath() + "/user/mainpage"; // 로그인 폼으로 리다이렉트
	}

	// 이메일 중복확인
	@RequestMapping("emailDupCheck")
	public String emailDupCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String email = request.getParameter("email");
		int result = userDAO.emailDupCheck(email);
		// ajax로 response가 현재 signupform보면
		// response가 avaliable이면 ajax가 사용가능한 이메일
		// avaliable이 아니면 ajax가 사용불가능한 이메일
		String msg = (result == 0) ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.";
		request.setAttribute("msg", msg);
		request.setAttribute("url", request.getContextPath() + "/user/signupform");

		return "user/plain"; // JSON 응답을 위해 JSP 페이지로 이동하지 않음
	}

}