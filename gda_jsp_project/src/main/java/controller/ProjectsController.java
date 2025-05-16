package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import gdu.mskim.MSLogin;
import model.dao.CommentDAO;
import model.dao.ProjectsDAO;
import model.dao.TagDAO;
import model.dto.CommentDTO;
import model.dto.ProjectsDTO;
import model.dto.TagDTO;
import model.dto.UserDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MybatisConnection;
import java.util.List;


@WebServlet(urlPatterns = {"/projects/*"}, initParams = {@WebInitParam(name = "view", value = "/view/")})
public class ProjectsController extends MskimRequestMapping {

	@RequestMapping("projectsList")
	public String projectsList(HttpServletRequest request, HttpServletResponse response) {
	    return "redirect:list?pageNum=1&keyword=&sort=recent&status=";
	}

	@RequestMapping("list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
	    int pageNum = request.getParameter("pageNum") != null ? Integer.parseInt(request.getParameter("pageNum")) : 1;
	    int pageSize = 10;

	    try (SqlSession session = MybatisConnection.getConnection()) {
	        ProjectsDAO dao = new ProjectsDAO(session);
	        PageHelper.startPage(pageNum, pageSize);

	        List<ProjectsDTO> list = dao.listAll();
	        PageInfo<ProjectsDTO> pageInfo = new PageInfo<>(list);

	        request.setAttribute("projects", pageInfo.getList());
	        request.setAttribute("pageInfo", pageInfo);
	        PageHelper.clearPage();
	        return "projects/projectsList";
	        
	    } 
	}

	@RequestMapping("Detail")
	public String detail(HttpServletRequest request, HttpServletResponse response) {
	    try (SqlSession session = MybatisConnection.getConnection()) {
	        int projectId = Integer.parseInt(request.getParameter("projectId"));

	        ProjectsDAO projectsDAO = new ProjectsDAO(session);
	        ProjectsDTO project = projectsDAO.findProjectWithLeaderName(projectId);

	        // ✅ 디버깅 로그 추가
	        System.out.println("[DEBUG] 조회된 프로젝트 정보: " + project);
	       
	        if (project == null) {
	            System.out.println("[ERROR] 프로젝트 조회 실패 - projectId: " + projectId);
	            return "redirect:projectsList";  // ✅ 실패 시 목록으로 리다이렉트
	        }

	        CommentDAO commentDAO = new CommentDAO(session);
	        List<CommentDTO> comments = commentDAO.getCommentsByProjectId(projectId);

	        TagDAO tagDAO = new TagDAO(session);
	        List<TagDTO> projectTags = tagDAO.getTagsByProjectId(projectId);

	        request.setAttribute("project", project);
	        request.setAttribute("comments", comments);
	        request.setAttribute("projectTags", projectTags);

	        return "projects/projectsDetail";  // ✅ 정상 처리 시 상세 페이지
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:projectsList";  // ✅ 예외 발생 시 목록으로 리다이렉트
	    }
	}



	    @RequestMapping("projectsForm")
	    public String Form(HttpServletRequest request, HttpServletResponse response) {
	        // ✅ 현재 로그인 유저 정보 출력 (테스트용)
	        Object userObj = request.getSession().getAttribute("user");
	        if (userObj == null) {
	            System.out.println("[DEBUG] 현재 로그인 사용자 없음");
	        } else {
	            System.out.println("[DEBUG] 현재 로그인 사용자: " + userObj.toString());
	        }
	        try (SqlSession session = MybatisConnection.getConnection()) {
	            TagDAO tagDAO = new TagDAO(session);
	            List<TagDTO> tagList = tagDAO.getAllTags();
	            request.setAttribute("tagList", tagList);
	            return "projects/projectsForm";
	        }
	    }

	    @RequestMapping("write")
	    public String write(HttpServletRequest request, HttpServletResponse response) throws Exception {
	        // ✅ 일반 텍스트 파라미터 수집
	        String title = request.getParameter("title");
	        String description = request.getParameter("description");
	        String[] tagIds = request.getParameterValues("tags");

	        // ✅ 로그인 유저 확인
	        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("user");
	        if (loginUser == null) {
	            return "redirect:/user/loginform";
	        }
	        int userId = loginUser.getUserId();

	        // ✅ DTO 생성 및 값 설정
	        ProjectsDTO project = new ProjectsDTO();
	        project.setTitle(title);
	        project.setDescription(description);
	        project.setLeaderId(userId);
	        project.setViewCount(0);

	        // ✅ 값 검증 로그
	        System.out.println("[DEBUG] 저장 전 DTO: " + project);

	        // ✅ DB 저장 처리
	        try (SqlSession session = MybatisConnection.getConnection()) {
	            ProjectsDAO projectsDAO = new ProjectsDAO(session);
	            projectsDAO.insert(project);

	            // ✅ 태그 매핑 처리
	            if (tagIds != null) {
	                TagDAO tagDAO = new TagDAO(session);
	                for (String tagIdStr : tagIds) {
	                    int tagId = Integer.parseInt(tagIdStr);
	                    tagDAO.insertMapping(project.getProjectId(), "PROJECT", tagId);
	                }
	            }

	            // ✅ 트랜잭션 커밋
	            session.commit();
	            return "redirect:projectsList";
	        }
	    }


    @RequestMapping("projectsEdit")
    public String updateForm(HttpServletRequest request, HttpServletResponse response) {
        try (SqlSession session = MybatisConnection.getConnection()) {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            ProjectsDAO dao = new ProjectsDAO(session);
            ProjectsDTO project = dao.findProjectWithLeaderName(projectId);
            request.setAttribute("project", project);
            return "projects/projectsEdit";
        }
    }

    @RequestMapping("edit")
    public String edit(HttpServletRequest request, HttpServletResponse response) {
        try (SqlSession session = MybatisConnection.getConnection()) {
            // 필수 파라미터 검증
            String projectIdStr = request.getParameter("projectId");
            if (projectIdStr == null || projectIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("projectId는 필수입니다.");
            }

            String leaderIdStr = request.getParameter("leaderId");
            if (leaderIdStr == null || leaderIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("leaderId는 필수입니다.");
            }

            String statusStr = request.getParameter("recruitStatus");
            ProjectsDTO.RecruitStatus status;
            try {
                status = ProjectsDTO.RecruitStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("잘못된 모집 상태입니다: " + statusStr);
            }

            ProjectsDTO project = new ProjectsDTO();
            project.setProjectId(Integer.parseInt(projectIdStr));
            project.setTitle(request.getParameter("title"));
            project.setDescription(request.getParameter("description"));
            project.setLeaderId(Integer.parseInt(leaderIdStr));
            project.setRecruitStatus(status);

            ProjectsDAO dao = new ProjectsDAO(session);
            dao.update(project);
            session.commit();

            return "redirect:Detail?projectId=" + project.getProjectId();
        }
    }

    @RequestMapping("delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        try (SqlSession session = MybatisConnection.getConnection()) {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            ProjectsDAO dao = new ProjectsDAO(session);
            dao.delete(projectId);
            session.commit();
            return "redirect:projectsList";
        }
    }
    
 // 댓글 등록 처리
    @RequestMapping("addComment")
    public String addComment(HttpServletRequest request, HttpServletResponse response) {
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("user");
        if (loginUser == null) {
            // 로그인하지 않았으면 로그인 페이지로 이동
            return "redirect:/user/loginform";
        }
        
        UserDTO loginUser1 = (UserDTO) request.getSession().getAttribute("user");
        if (loginUser1 == null) {
            System.out.println("[ERROR] 비로그인 상태 - 세션 없음");
            return "redirect:/user/loginform";
        }

        System.out.println("[DEBUG] 세션 유저 ID: " + loginUser1.getUserId());

        int userId = loginUser1.getUserId();
        // 디버깅: users 테이블에 userId가 존재하는지 DB에서 먼저 확인

        

        int projectId = Integer.parseInt(request.getParameter("projectId"));
        String content = request.getParameter("commentContent");
        int user_Id = loginUser1.getUserId();

        System.out.println("[DEBUG] 댓글 작성 시도 - userId: " + userId);

        CommentDTO comment = new CommentDTO();
        comment.setProjectId(projectId);
        comment.setContent(content);
        comment.setUserId(userId);

        try (SqlSession session = MybatisConnection.getConnection()) {
            CommentDAO commentDAO = new CommentDAO(session);
            commentDAO.insert(comment);
            session.commit();
        }

        return "redirect:Detail?projectId=" + projectId;
    }

 // 댓글 삭제 처리
    @RequestMapping("deleteComment")
    public String deleteComment(HttpServletRequest request, HttpServletResponse response) {
        try (SqlSession session = MybatisConnection.getConnection()) {
            int commentId = Integer.parseInt(request.getParameter("commentId"));
            int projectId = Integer.parseInt(request.getParameter("projectId"));

            CommentDAO commentDAO = new CommentDAO(session);
            commentDAO.softDelete(commentId);
            session.commit();

            return "redirect:Detail?projectId=" + projectId;
        }
    }

}
