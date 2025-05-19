package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@MultipartConfig // ✅ 여기에 추가
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
	    String sort = request.getParameter("sort") != null ? request.getParameter("sort") : "recent";
	    String status = request.getParameter("status");

	    try (SqlSession session = MybatisConnection.getConnection()) {
	        ProjectsDAO dao = new ProjectsDAO(session);
	        PageHelper.startPage(pageNum, pageSize);

	        List<ProjectsDTO> list;
	        if (status != null && !status.isEmpty()) {
	            if ("views".equals(sort)) {
	                list = dao.listByStatusOrderByViews(status);
	            } else {
	                list = dao.listByStatusOrderByRecent(status);
	            }
	        } else {
	            if ("views".equals(sort)) {
	                list = dao.listOrderByViews();
	            } else {
	                list = dao.listOrderByRecent();
	            }
	        }

	        PageInfo<ProjectsDTO> pageInfo = new PageInfo<>(list);
	        
	     // ✅ description에서 태그 제거 및 줄바꿈 제거 처리
	        for (ProjectsDTO project : pageInfo.getList()) {
	            if (project.getDescription() != null) {
	                String cleaned = project.getDescription()
	                                        .replaceAll("<[^>]*>", "")  // 태그 제거
	                                        .replaceAll("[\\n\\r]", ""); // 줄바꿈 제거
	                project.setDescription(cleaned);
	            }
	        }
	        
	        request.setAttribute("projects", pageInfo.getList());
	        request.setAttribute("pageInfo", pageInfo);
	        PageHelper.clearPage();
	        return "projects/projectsList";
	    }
	}




	@RequestMapping("detail")
	public String detail(HttpServletRequest request, HttpServletResponse response) {
	    try (SqlSession session = MybatisConnection.getConnection()) {
	        int projectId = Integer.parseInt(request.getParameter("projectId"));

	        ProjectsDAO projectsDAO = new ProjectsDAO(session);
	        // ✅ 조회수 증가 먼저 호출
	        projectsDAO.incrementViewCount(projectId);
	        session.commit();  // 💡 반드시 commit 해야 실제 DB에 반영됨
	        // ✅ 글 상세 조회
	        ProjectsDTO project = projectsDAO.findProjectWithLeaderName(projectId);

	        if (project == null) {
	            return "redirect:projectsList";
	        }

	        CommentDAO commentDAO = new CommentDAO(session);
	        List<CommentDTO> comments = commentDAO.getCommentsByProjectId(projectId);

	        TagDAO tagDAO = new TagDAO(session);
	        List<TagDTO> projectTags = tagDAO.getTagsByProjectId(projectId);

	        // 현재 로그인 유저 ID 전달
	        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("user");
	        if (loginUser != null) {
	            request.setAttribute("loginUserId", loginUser.getUserId());
	        }

	        request.setAttribute("project", project);
	        request.setAttribute("comments", comments);
	        request.setAttribute("projectTags", projectTags);

	        return "projects/projectsDetail";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:projectsList";
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
	    
	    @RequestMapping("uploadImage")
	    public void uploadImage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	        Part filePart = request.getPart("file"); // ✅ 'file'로 변경
	        if (filePart == null) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().write("파일이 없습니다.");
	            return;
	        }

	        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
	        String uploadPath = "C:/java_lec/workspace/gda_jsp_project/src/main/webapp/static/images";

	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) uploadDir.mkdirs();

	        String filePath = uploadPath + File.separator + fileName;
	        filePart.write(filePath);

	        String imageUrl = request.getContextPath() + "/static/images/" + fileName;
	        response.setContentType("text/plain;charset=UTF-8");
	        response.getWriter().write(imageUrl);

	        System.out.println("파일 저장 경로: " + filePath);
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

	            ProjectsDAO projectsDAO = new ProjectsDAO(session);
	            TagDAO tagDAO = new TagDAO(session);

	            ProjectsDTO project = projectsDAO.findProjectWithLeaderName(projectId);
	            List<TagDTO> tagList = tagDAO.getAllTags();
	            List<TagDTO> projectTags = tagDAO.getTagsByProjectId(projectId);

	            request.setAttribute("project", project);
	            request.setAttribute("tagList", tagList);         // ✅ 드롭다운 출력용
	            request.setAttribute("projectTags", projectTags); // ✅ 선택된 태그 출력용

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

	            // 프로젝트 기본 정보 설정
	            ProjectsDTO project = new ProjectsDTO();
	            project.setProjectId(Integer.parseInt(projectIdStr));
	            project.setTitle(request.getParameter("title"));
	            project.setDescription(request.getParameter("description"));
	            project.setLeaderId(Integer.parseInt(leaderIdStr));
	            project.setRecruitStatus(status);

	            ProjectsDAO dao = new ProjectsDAO(session);
	            dao.update(project);

	            // 태그 매핑 갱신
	            String[] tagIds = request.getParameterValues("tags");
	            TagDAO tagDAO = new TagDAO(session);

	            // 기존 태그 매핑 삭제
	            tagDAO.deleteMappings(project.getProjectId(), "PROJECT");

	            // 새로운 태그 매핑 추가
	            if (tagIds != null) {
	                for (String tagIdStr : tagIds) {
	                    int tagId = Integer.parseInt(tagIdStr);
	                    tagDAO.insertMapping(project.getProjectId(), "PROJECT", tagId);
	                }
	            }

	            session.commit();
	            return "redirect:detail?projectId=" + project.getProjectId();
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

        return "redirect:detail?projectId=" + projectId;
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

            return "redirect:detail?projectId=" + projectId;
        }
    }

}
