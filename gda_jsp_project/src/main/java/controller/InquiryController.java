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
import java.time.LocalDateTime;
import java.util.List;

/**
 * 📋 InquiryController
 * - 강의 문의 목록, 작성, 삭제 통합 컨트롤러
 */
@WebServlet(
    urlPatterns = "/lecture/inquiry/*",
    initParams = @WebInitParam(name = "view", value = "/view/")
)
public class InquiryController extends MskimRequestMapping {

    private final InquiryService inquiryService = new InquiryService();

    /**
     * ✅ 문의글 목록 출력 (페이징 포함)
     * URL: /lecture/inquiry/list
     */
    @RequestMapping("list")
    public String listInquiries(HttpServletRequest req, HttpServletResponse res) throws Exception {
        int page = 1;
        int size = 10;

        String pageParam = req.getParameter("page");
        if (pageParam != null && pageParam.matches("\\d+")) {
            page = Integer.parseInt(pageParam);
        }

        List<InquiryDTO> inquiryList = inquiryService.getPagedInquiries(page, size);
        int totalCount = inquiryService.getTotalInquiries();
        int totalPages = (int) Math.ceil(totalCount / (double) size);

        req.setAttribute("inquiryList", inquiryList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        return "lecture/inquiryList";
    }

    @RequestMapping("write")
    public String handleInquiryWrite(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return "lecture/inquiryWrite"; // 작성 폼
        }

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            UserDTO loginUser = (UserDTO) request.getSession().getAttribute("loginUser");
            if (loginUser == null) {
                throw new IllegalStateException("로그인 후 작성 가능합니다.");
            }

            InquiryDTO dto = new InquiryDTO();
            dto.setUserId(loginUser.getUserId()); // DTO에 맞게
            dto.setLectureId(Integer.parseInt(request.getParameter("lectureId")));
            dto.setTitle(request.getParameter("title"));
            dto.setContent(request.getParameter("content"));
            dto.setType("LECTURE");
            dto.setCreatedAt(LocalDateTime.now());

            inquiryService.insertInquiry(dto);

            return "redirect:inquiryList"; // ✅ 등록 후에는 목록으로 이동
        }

        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return null;
    }

    @RequestMapping("inquirywrite")
    public String handleNewInquiryWrite(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            // 📄 새 폼으로 이동
            return "lecture/inquiryWrite"; // 또는 inquiryWrite2.jsp 로 분리 가능
        }

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            UserDTO loginUser = (UserDTO) request.getSession().getAttribute("loginUser");
            if (loginUser == null) {
                throw new IllegalStateException("로그인 후 작성 가능합니다.");
            }

            InquiryDTO dto = new InquiryDTO();
            dto.setUserId(loginUser.getUserId());
            dto.setLectureId(Integer.parseInt(request.getParameter("lectureId")));
            dto.setTitle(request.getParameter("title"));
            dto.setContent(request.getParameter("content"));
            dto.setType("LECTURE");
            dto.setCreatedAt(LocalDateTime.now());

            inquiryService.insertInquiry(dto);
            return "redirect:inquiryList";
        }

        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return null;
    }

    /**
     * ✅ 문의글 삭제 (목록에서 삭제)
     * URL: /lecture/inquiry/delete
     */
    @RequestMapping("delete")
    public String deleteInquiry(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idParam = request.getParameter("inquiryId");
        if (idParam == null || !idParam.matches("\\d+")) {
            throw new IllegalArgumentException("❌ 유효하지 않은 ID입니다.");
        }

        int inquiryId = Integer.parseInt(idParam);
        inquiryService.deleteInquiry(inquiryId);

        return "redirect:inquiryList";
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
    /**
     * ✅ 문의글 삭제 (강의 상세 페이지 문의 탭)
     * URL: /lecture/inquiry/inquiry/delete
     */
    @RequestMapping("inquiry/delete")
    public String inquiryDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        int inquiryId = Integer.parseInt(request.getParameter("inquiryId"));
        int lectureId = Integer.parseInt(request.getParameter("lectureId"));

        inquiryService.deleteInquiry(inquiryId);

        return "redirect:/lecture/inquiries?lectureId=" + lectureId;
    }
} 