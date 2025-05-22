package controller;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.dto.InquiryDTO;
import model.dto.UserDTO;
import service.InquiryService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 📋 InquiryController - 강의 문의 목록, 작성, 삭제 통합 컨트롤러
 */
@WebServlet(urlPatterns = "/lecture/inquiry/*", initParams = @WebInitParam(name = "view", value = "/view/"))
public class InquiryController extends MskimRequestMapping {

	private final InquiryService inquiryService = new InquiryService();

	/**
	 * ✅ 문의글 목록 출력 (페이징 포함) URL: /lecture/inquiry/list
	 */
	@RequestMapping("list")
	public String listInquiries(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int page = 1;
		int size = 10;

		String lectureIdStr = request.getParameter("lectureId");
		if (lectureIdStr == null || lectureIdStr.isBlank()) {
			throw new IllegalArgumentException("lectureId가 전달되지 않았습니다.");
		}

		System.out.println(Integer.parseInt(lectureIdStr));

		String pageParam = request.getParameter("page");
		if (pageParam != null && pageParam.matches("\\d+")) {
			page = Integer.parseInt(pageParam);
		}

		List<InquiryDTO> inquiryList = inquiryService.getPagedInquiries(page, size);
		int totalCount = inquiryService.getTotalInquiries();
		int totalPages = (int) Math.ceil(totalCount / (double) size);

		request.setAttribute("inquiryList", inquiryList);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("lectureId", lectureIdStr);

		return "lecture/inquiryList";
	}

	@RequestMapping("write")
	public String handleInquiryWrite(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");

		String lectureIdStr = request.getParameter("lectureId");

		if ("GET".equalsIgnoreCase(request.getMethod())) {
			// 수정 모드인지 확인
			String editId = request.getParameter("editId");
			if (editId != null && editId.matches("\\d+")) {
				int inquiryId = Integer.parseInt(editId);
				InquiryDTO inquiry = inquiryService.getInquiryById(inquiryId);
				request.setAttribute("inquiry", inquiry);
			}

			// lectureId를 JSP에 전달
			request.setAttribute("lectureId", Integer.parseInt(lectureIdStr));

			return "lecture/inquiryWrite"; // 문의 작성 JSP 포워딩
		}

		if ("POST".equalsIgnoreCase(request.getMethod())) {
			UserDTO loginUser = (UserDTO) request.getSession().getAttribute("user");
			if (loginUser == null) {
				throw new IllegalStateException("로그인 후 작성 가능합니다.");
			}

			InquiryDTO dto = new InquiryDTO();
			dto.setUserId(loginUser.getUserId());
			dto.setLectureId(Integer.parseInt(lectureIdStr));
			dto.setTitle(request.getParameter("title"));
			dto.setContent(request.getParameter("content"));

			String editId = request.getParameter("editId");
			if (editId != null && editId.matches("\\d+")) {
				// 수정 처리
				dto.setInquiryId(Integer.parseInt(editId));
				inquiryService.updateInquiry(dto);
			} else {
				// 새 글 등록 처리
				dto.setType("LECTURE");
				dto.setCreatedAt(LocalDateTime.now());
				inquiryService.insertInquiry(dto);
			}

			// 등록/수정 후 문의 리스트로 redirect (lectureId 파라미터 포함)
			return "redirect:" + request.getContextPath() + "/lecture/inquiry/list?lectureId=" + lectureIdStr;
		}

		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	@RequestMapping("inquirywrite")
	public String handleNewInquiryWrite(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");

		String lectureIdStr = request.getParameter("lectureId");
		if (lectureIdStr == null || lectureIdStr.isBlank()) {
			throw new IllegalArgumentException("lectureId가 전달되지 않았습니다.");
		}

		if ("GET".equalsIgnoreCase(request.getMethod())) {
			request.setAttribute("lectureId", lectureIdStr); // << 이 부분이 중요!
			return "lecture/inquiryWrite";
		}

		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	/**
	 * ✅ 문의글 삭제 (목록에서 삭제) URL: /lecture/inquiry/delete
	 */
	@RequestMapping("delete")
	public String deleteInquiry(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String lectureIdStr = request.getParameter("lectureId");
		if (lectureIdStr == null || lectureIdStr.isBlank()) {
			throw new IllegalArgumentException("lectureId가 전달되지 않았습니다.");
		}
		
		String idParam = request.getParameter("inquiryId");
		if (idParam == null || !idParam.matches("\\d+")) {
			throw new IllegalArgumentException("❌ 유효하지 않은 ID입니다.");
		}
		

		int inquiryId = Integer.parseInt(idParam);
		inquiryService.deleteInquiry(inquiryId);

		request.getSession().setAttribute("msg", "삭제가 완료되었습니다.");

		String path = request.getContextPath();

		return "redirect:" + path + "/lecture/inquiry/list?lectureId="+lectureIdStr; // ✅ 등록 후에는 목록으로 이동
	}

	@RequestMapping("detail")
	public String inquiryDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String param = request.getParameter("inquiryId");
		if (param == null || !param.matches("\\d+")) {
			throw new IllegalArgumentException("유효한 문의 ID가 필요합니다.");
		}

		int inquiryId = Integer.parseInt(param);
		InquiryDTO inquiry = inquiryService.getInquiryById(inquiryId);
		if (inquiry == null) {
			throw new IllegalArgumentException("해당 문의글을 찾을 수 없습니다.");
		}
		
		

		request.setAttribute("inquiry", inquiry);
		return "lecture/inquiryDetail";
	}

}