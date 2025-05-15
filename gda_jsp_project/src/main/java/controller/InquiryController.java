package controller;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.dto.InquiryDTO;
import service.InquiryService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 📋 InquiryController
 * - 강의 문의 목록 조회, 삭제 처리 담당
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
    public String inquiryList(HttpServletRequest request) throws Exception {
        int page = 1;
        int limit = 10;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isBlank()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1; // 잘못된 입력은 기본값으로 처리
            }
        }

        int offset = (page - 1) * limit;

        List<InquiryDTO> inquiryList = inquiryService.getPagedInquiries(limit, offset);
        int totalCount = inquiryService.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        request.setAttribute("inquiryList", inquiryList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        return "lecture/inquiryList"; // 📄 /view/lecture/inquiryList.jsp
    }

    /**
     * ✅ 문의글 삭제 처리 (일반 목록에서 삭제)
     * URL: /lecture/inquiry/delete?inquiryId=3
     */
    @RequestMapping("delete")
    public String deleteInquiry(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idParam = request.getParameter("inquiryId");
        if (idParam == null || !idParam.matches("\\d+")) {
            throw new IllegalArgumentException("❌ 유효하지 않은 ID입니다.");
        }

        int inquiryId = Integer.parseInt(idParam);
        inquiryService.deleteInquiry(inquiryId);

        return "redirect:list"; // 📌 상대 경로 리다이렉트
    }

    /**
     * ✅ 문의글 삭제 처리 (특정 강의의 문의 탭에서 삭제한 경우)
     * URL: /lecture/inquiry/inquiry/delete?inquiryId=3&lectureId=10
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
