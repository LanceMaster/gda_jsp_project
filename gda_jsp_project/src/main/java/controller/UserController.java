package controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
		// 업로드 폴더 경로 설정
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
			return "user/signupform";
		}

		// 파라미터 추출
		String email = multi.getParameter("email");
		String password = multi.getParameter("password");
		String role = multi.getParameter("role"); // "STUDENT" 또는 "INSTRUCTOR"
		String resumeFileName = multi.getFilesystemName("resume"); // 업로드된 이력서 파일 이름

		// 필수 항목 체크
		if (email == null || password == null || role == null || email.trim().isEmpty() || password.trim().isEmpty()) {
			request.setAttribute("signupError", "이메일, 비밀번호, 역할은 필수 입력입니다.");
			return "redirect:" + request.getContextPath() + "/user/signupform"; // 로그인 폼으로 리다이렉트

		}

		// 이력서 파일 체크 (강사만 필수)
		if ("INSTRUCTOR".equalsIgnoreCase(role) && (resumeFileName == null || resumeFileName.trim().isEmpty())) {
			request.setAttribute("signupError", "강사 신청 시 이력서 파일은 필수입니다.");
			return "redirect:" + request.getContextPath() + "/user/signupform"; // 로그인 폼으로 리다이렉트

		}

		// DTO 설정
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(email);
		userDTO.setPassword(password);
		userDTO.setName(multi.getParameter("name"));
		userDTO.setPhone(multi.getParameter("phone"));
		userDTO.setRole(role.toUpperCase()); // "STUDENT" or "INSTRUCTOR"
		userDTO.setResume(resumeFileName != null ? resumeFileName : "");
		userDTO.setAgreedTerms(true); // 약관 동의 여부
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			userDTO.setBirthdate(sdf.parse(multi.getParameter("birthdate")));
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:" + request.getContextPath() + "/user/signupform"; // 로그인 폼으로 리다이렉트
		}

		int result = userDAO.signup(userDTO);

		if (result > 0) {
			return "redirect:" + request.getContextPath() + "/user/loginform";
		} else {
			request.setAttribute("signupError", "회원가입 실패");
			return "user/signupform";
		}
	}

	// 아이디찾기폼 불러오기
	@RequestMapping("findidform")
	public String findidform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 아이디 찾기 폼을 보여주는 JSP 페이지로 이동
		return "user/findidform"; // JSP 페이지 경로
	}

//	//회원탈퇴 처리
//	@RequestMapping("delete")
//	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String id = request.getParameter("id");
//		String password = request.getParameter("password");
//
//		UserDTO userDTO = userDAO.login(id, password);
//
//		if (userDTO != null) {
//			userDAO.delete(id);
//			request.getSession().invalidate(); // 세션 무효화
//			return "redirect:" + request.getContextPath() + "/user/mainpage"; // 메인 페이지로 리다이렉트
//		} else {
//			request.setAttribute("deleteError", "아이디 또는 비밀번호를 확인해주세요");
//			return "user/deleteform"; // 회원탈퇴 폼으로 리다이렉트
//		}
//	}
//	

	// 로그아웃 처리
	@RequestMapping("logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().invalidate(); // 세션 무효화
		return "redirect:" + request.getContextPath() + "/user/mainpage"; // 로그인 폼으로 리다이렉트
	}

	// 이메일 중복확인
//	@RequestMapping("emailDupCheck")
//	public String emailDupCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String email = request.getParameter("email");
//		int result = userDAO.emailDupCheck(email);
//		// ajax로 response가 현재 signupform보면
//		// response가 avaliable이면 ajax가 사용가능한 이메일
//		// avaliable이 아니면 ajax가 사용불가능한 이메일
//		String msg = (result == 0) ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.";
//		request.setAttribute("msg", msg);
//		request.setAttribute("url", request.getContextPath() + "/user/signupform");
//
//		return "user/plain"; // JSON 응답을 위해 JSP 페이지로 이동하지 않음
//	}

}