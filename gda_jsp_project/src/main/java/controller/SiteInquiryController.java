package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;

import model.dao.SiteInquiryDAO;
import model.dao.InquiryCommentDAO;
import model.dto.SiteInquiryDTO;
import model.dto.InquiryCommentDTO;
import model.dto.UserDTO;

import org.apache.ibatis.session.SqlSession;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import utils.MybatisConnection;

import java.util.List;

@WebServlet(urlPatterns = {"/siteinquiry/*"}, initParams = {@WebInitParam(name = "view", value = "/view/")})
public class SiteInquiryController extends MskimRequestMapping {

    // 기본 리스트 진입 시 최신글 목록 (pageNum=1, keyword="")
    @RequestMapping("siteinquiryList")
    public String siteinquiryList(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:list?pageNum=1&keyword=";
    }

    // 페이징 포함 리스트 (검색어 2글자 이상부터 검색, 기본 최신순)
    @RequestMapping("list")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        int pageNum = request.getParameter("pageNum") != null ? Integer.parseInt(request.getParameter("pageNum")) : 1;
        int pageSize = 10;
        String keyword = request.getParameter("keyword") != null ? request.getParameter("keyword").trim() : "";
        String filterUnanswered = request.getParameter("filterUnanswered");

        boolean hasKeyword = !keyword.isEmpty();
        boolean isKeywordValid = keyword.length() >= 2;

        try (SqlSession session = MybatisConnection.getConnection()) {
            SiteInquiryDAO dao = new SiteInquiryDAO(session);

            PageHelper.startPage(pageNum, pageSize);

            List<SiteInquiryDTO> list;

            if ("true".equals(filterUnanswered)) {
                // 미답변만 오래된 순 조회 (관리자 전용)
                list = dao.listUnansweredOrderByOldest();
            } else if (hasKeyword) {
                if (!isKeywordValid) {
                    request.setAttribute("errorMessage", "검색어는 최소 2글자 이상 입력해주세요.");
                    list = dao.listAll();  // 경고 후 전체 최신순
                } else {
                    list = dao.searchByTitleOrContent(keyword);  // 키워드 검색
                }
            } else {
                list = dao.listAll();  // 기본 최신순 전체 목록
            }

            PageInfo<SiteInquiryDTO> pageInfo = new PageInfo<>(list);

            request.setAttribute("inquiries", pageInfo.getList());
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("keyword", keyword);
            request.setAttribute("filterUnanswered", filterUnanswered);

            PageHelper.clearPage();

            return "siteinquiry/siteinquiryList";
        }
    }


    // 상세보기 + 댓글 리스트
    @RequestMapping("detail")
    public String detail(HttpServletRequest request, HttpServletResponse response) {
        int inquiryId = Integer.parseInt(request.getParameter("inquiryId"));
        try (SqlSession session = MybatisConnection.getConnection()) {
            SiteInquiryDAO dao = new SiteInquiryDAO(session);
            SiteInquiryDTO inquiry = dao.findById(inquiryId);
            if (inquiry == null) return "redirect:list";

            InquiryCommentDAO commentDAO = new InquiryCommentDAO(session);
            List<InquiryCommentDTO> comments = commentDAO.getCommentsByInquiryId(inquiryId);

            UserDTO loginUser = (UserDTO) request.getSession().getAttribute("user");
            if (loginUser != null) {
                request.setAttribute("loginUserId", loginUser.getUserId());
            }
            request.setAttribute("inquiry", inquiry);
            request.setAttribute("comments", comments);
            return "siteinquiry/siteinquiryDetail";
        }
    }

    // 작성폼 (로그인 체크 후 폼 페이지)
    @RequestMapping("writeForm")
    public String writeForm(HttpServletRequest request, HttpServletResponse response) {
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("user");
        if (loginUser == null) {
            return "redirect:" + request.getContextPath() + "/user/loginform";
        }
        request.setAttribute("loginUser", loginUser);
        return "siteinquiry/siteinquiryForm";
    }

    // 등록 처리 (로그인 체크 후 데이터 저장)
    @RequestMapping("write")
    public String write(HttpServletRequest request, HttpServletResponse response) {
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("user");
        if (loginUser == null) {
            return "redirect:" + request.getContextPath() + "/user/loginform";
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        SiteInquiryDTO dto = new SiteInquiryDTO();
        dto.setUserId(loginUser.getUserId());
        dto.setTitle(title);
        dto.setContent(content);
        dto.setType("SITE");

        try (SqlSession session = MybatisConnection.getConnection()) {
            SiteInquiryDAO dao = new SiteInquiryDAO(session);
            dao.insert(dto);
            session.commit();
        }
        return "redirect:list";
    }

    // 수정폼
    @RequestMapping("editForm")
    public String editForm(HttpServletRequest request, HttpServletResponse response) {
        int inquiryId = Integer.parseInt(request.getParameter("inquiryId"));
        try (SqlSession session = MybatisConnection.getConnection()) {
            SiteInquiryDAO dao = new SiteInquiryDAO(session);
            SiteInquiryDTO inquiry = dao.findById(inquiryId);
            request.setAttribute("inquiry", inquiry);
            return "siteinquiry/siteinquiryEdit";
        }
    }

    // 수정 처리
    @RequestMapping("edit")
    public String edit(HttpServletRequest request, HttpServletResponse response) {
        int inquiryId = Integer.parseInt(request.getParameter("inquiryId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        SiteInquiryDTO dto = new SiteInquiryDTO();
        dto.setInquiryId(inquiryId);
        dto.setTitle(title);
        dto.setContent(content);

        try (SqlSession session = MybatisConnection.getConnection()) {
            SiteInquiryDAO dao = new SiteInquiryDAO(session);
            dao.update(dto);
            session.commit();
            return "redirect:detail?inquiryId=" + inquiryId;
        }
    }

    // 삭제 처리
    @RequestMapping("delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        int inquiryId = Integer.parseInt(request.getParameter("inquiryId"));
        try (SqlSession session = MybatisConnection.getConnection()) {
            SiteInquiryDAO dao = new SiteInquiryDAO(session);
            dao.delete(inquiryId);
            session.commit();
            return "redirect:list";
        }
    }

    // 댓글 등록
    @RequestMapping("addComment")
    public String addComment(HttpServletRequest request, HttpServletResponse response) {
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("user");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            return "redirect:" + request.getContextPath() + "/user/loginform";
        }
        int inquiryId = Integer.parseInt(request.getParameter("inquiryId"));
        String content = request.getParameter("commentContent");

        InquiryCommentDTO comment = new InquiryCommentDTO();
        comment.setInquiryId(inquiryId);
        comment.setUserId(loginUser.getUserId());
        comment.setContent(content);

        try (SqlSession session = MybatisConnection.getConnection()) {
            InquiryCommentDAO commentDAO = new InquiryCommentDAO(session);
            commentDAO.insert(comment);

            // 답변 여부 업데이트 (댓글 등록 시 true)
            SiteInquiryDAO inquiryDAO = new SiteInquiryDAO(session);
            inquiryDAO.updateIsAnswered(inquiryId, true);

            session.commit();
        }
        return "redirect:detail?inquiryId=" + inquiryId;
    }

    // 댓글 삭제(soft delete)
    @RequestMapping("deleteComment")
    public String deleteComment(HttpServletRequest request, HttpServletResponse response) {
        int commentId = Integer.parseInt(request.getParameter("commentId"));
        int inquiryId = Integer.parseInt(request.getParameter("inquiryId"));

        try (SqlSession session = MybatisConnection.getConnection()) {
            InquiryCommentDAO commentDAO = new InquiryCommentDAO(session);
            SiteInquiryDAO inquiryDAO = new SiteInquiryDAO(session);

            // 댓글 삭제 (soft delete)
            commentDAO.softDelete(commentId);

            // 삭제 후 남은 활성 댓글 수 체크
            int remainingComments = commentDAO.getCommentsByInquiryId(inquiryId).size();

            // 남은 댓글 없으면 답변여부 false로 변경
            if (remainingComments == 0) {
                inquiryDAO.updateIsAnswered(inquiryId, false);
            }

            session.commit();
        }
        return "redirect:detail?inquiryId=" + inquiryId;
    }
}
