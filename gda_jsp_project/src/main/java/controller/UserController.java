package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.dao.LectureDAO;
import model.dao.NaverUserDAO;
import model.dao.UserDAO;
import model.dto.UserDTO;
import model.dto.LectureDTO;
import model.dto.NaverUserDTO;

@WebServlet(urlPatterns = { "/user/*" }, initParams = { @WebInitParam(name = "view", value = "/view/") })
public class UserController extends MskimRequestMapping {

	public UserDAO userDAO = new UserDAO();
	public NaverUserDAO naverUserDAO = new NaverUserDAO();
	public LectureDAO lectureDAO = new LectureDAO();

	// 메인페이지 불러오기
	@RequestMapping("mainpage")
	public String mainpage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 메인 페이지를 보여주는 JSP 페이지로 이동
		List<LectureDTO> topLectures = lectureDAO.getTopLectures(8); // 상위 8개 강의
		// 최신강의 8개 가져오기

		List<LectureDTO> latestLectures = lectureDAO.getLatestLectures(8); // 최신 8개 강의

		request.setAttribute("topLectures", topLectures);
		request.setAttribute("latestLectures", latestLectures);

		return "user/mainpage"; // JSP 페이지 경로
	}

	// 로그인폼 불러오기
	@RequestMapping("loginform")
	public String loginform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 로그인 폼을 보여주는 JSP 페이지로 이동
		return "user/loginform"; // JSP 페이지 경로
	}

	/**
	 * 로그인
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("login")
	public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String password = request.getParameter("password");

		UserDTO userDTO = userDAO.login(id, password); // DB 조회

		if (userDTO == null) {
			// 로그인 실패
			request.setAttribute("loginError", "아이디 또는 비밀번호를 확인해주세요");
			return "user/loginform";
		}

		// 로그인 성공 처리
		HttpSession session = request.getSession();
		session.setAttribute("user", userDTO);

		// 탈퇴 여부 확인
		if (userDTO.isDeleted()) {
			Date deletedAtDate = userDTO.getDeletedAt();
			if (deletedAtDate != null) {
				// Date → LocalDateTime 변환
				LocalDateTime deletedAt = deletedAtDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

				Duration duration = Duration.between(deletedAt, LocalDateTime.now());

				if (duration.toDays() >= 15) {
					// 15일 경과 → 완전 삭제된 계정
					request.setAttribute("loginError", "해당 계정은 탈퇴한 지 15일이 지나 삭제되었습니다.");
					session.invalidate(); // 세션 초기화
					return "user/loginform";
				} else {
					// 15일 미만 → 로그인은 가능, 경고 메시지
					session.setAttribute("withdrawalNotice", "탈퇴 요청된 계정입니다. 복구를 원하시면 '계정 삭제 취소' 버튼을 눌러주세요.");
				}
			}
		}

		// 리다이렉트 처리
		String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
		if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
			session.removeAttribute("redirectAfterLogin");
			return "redirect:" + redirectUrl;
		}

		return "redirect:" + request.getContextPath() + "/user/mainpage";
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
		String path = request.getServletContext().getRealPath("/") + "/upload/resume/";
		File uploadDir = new File(path);
		if (!uploadDir.exists())
			uploadDir.mkdirs();

		MultipartRequest multi;
		try {
			int maxSize = 10 * 1024 * 1024; // 10MB
			multi = new MultipartRequest(request, path, maxSize, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			request.setAttribute("signupError", "파일 업로드 실패");
			return "user/signupform";
		}

		// 파라미터 추출
		String email = multi.getParameter("email");
		String password = multi.getParameter("password");
		String role = multi.getParameter("role");
		String resumeFileName = multi.getFilesystemName("resume");
		String birthdateStr = multi.getParameter("birthdate");

		// 필수 항목 체크
		if (isNullOrEmpty(email) || isNullOrEmpty(password) || isNullOrEmpty(role)) {
			request.setAttribute("signupError", "이메일, 비밀번호, 역할은 필수 입력입니다.");
			return "redirect:" + request.getContextPath() + "/user/signupform";
		}

		// 이력서 파일 필수 체크 (강사인 경우)
		if ("INSTRUCTOR".equalsIgnoreCase(role) && isNullOrEmpty(resumeFileName)) {
			request.setAttribute("signupError", "강사 신청 시 이력서 파일은 필수입니다.");
			return "redirect:" + request.getContextPath() + "/user/signupform";
		}

		// 사용자 DTO 생성
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(email);
		userDTO.setPassword(password);
		userDTO.setName(multi.getParameter("name"));
		userDTO.setPhone(multi.getParameter("phone"));
		userDTO.setRole(role.toUpperCase());
		userDTO.setResume(resumeFileName != null ? resumeFileName : "");
		userDTO.setAgreedTerms(true);

		try {
			if (!isNullOrEmpty(birthdateStr)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				userDTO.setBirthdate(sdf.parse(birthdateStr));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			request.setAttribute("signupError", "생년월일 형식이 잘못되었습니다.");
			return "redirect:" + request.getContextPath() + "/user/signupform";
		}

		int result = userDAO.signup(userDTO);

		if (result > 0) {
			return "redirect:" + request.getContextPath() + "/user/loginform";
		} else {
			request.setAttribute("signupError", "회원가입 실패");
			return "user/signupform";
		}
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
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

	// 마이페이지 불러오기 프로필페이지

	@RequestMapping("mypage")
	public String mypage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserDTO sessionUser = (UserDTO) session.getAttribute("user");

		if (sessionUser == null) {
			return "redirect:" + request.getContextPath() + "/user/loginform"; // 로그인 폼으로 리다이렉트
		}

		// DB에서 최신 사용자 정보 조회
		int userId = sessionUser.getUserId();
		UserDTO userDTO = userDAO.getUserInformation(userId); // 예: userDAO에 해당 메서드가 있다고 가정
		NaverUserDTO naverUserDTO = NaverUserDAO.getInformationByEmail(sessionUser.getEmail());

		if (userDTO != null) {

			// 세션에 최신 정보 갱신
			session.setAttribute("user", userDTO);

			// 디버깅
			System.out.println(userDTO.toString());

			// 자기가 등록한 강의 목록 갖고오기
			List<LectureDTO> myLectures = lectureDAO.getMyLectures(userDTO.getUserId());
			// 내가 신청한 강의 목록 갖고오기
			List<LectureDTO> myCourses = lectureDAO.getMyCourses(userDTO.getUserId());

			// 요청 속성에 저장
			request.setAttribute("myLectures", myLectures);
			request.setAttribute("myCourses", myCourses);
		}

		if (userDTO == null && naverUserDTO == null) {

			session.invalidate(); // 사용자 정보가 더 이상 유효하지 않다면 세션 무효화
			return "redirect:" + request.getContextPath() + "/user/loginform";

		} else if (userDTO == null && naverUserDTO != null) {

			UserDTO navertouserDTO = new UserDTO();
			navertouserDTO.setRole("NAVER");
			navertouserDTO.setName(naverUserDTO.getName());
			navertouserDTO.setEmail(naverUserDTO.getEmail());
			navertouserDTO.setPhone(sessionUser.getPhone());
			navertouserDTO.setBirthdate(sessionUser.getBirthdate());
			session.setAttribute("user", navertouserDTO);

		}

		return "user/mypage"; // JSP 페이지 경로
	}

	// siteabout
	@RequestMapping("about")
	public String about(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// view 폴더 아래 user/about.jsp 를 렌더링
		return "user/about";
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