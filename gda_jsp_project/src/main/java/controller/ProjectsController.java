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
import model.dao.ProjectsDAO;
import model.dao.TagDAO;
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
    public String info(HttpServletRequest request, HttpServletResponse response) {
        try (SqlSession session = MybatisConnection.getConnection()) {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            ProjectsDAO dao = new ProjectsDAO(session);
            ProjectsDTO project = dao.listById(projectId);
            request.setAttribute("project", project);
            return "projects/Detail";
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
	        int userId = loginUser.getUser_id();

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
            ProjectsDTO project = dao.listById(projectId);
            request.setAttribute("project", project);
            return "projects/projectsEdit";
        }
    }

    @RequestMapping("Edit")
    public String update(HttpServletRequest request, HttpServletResponse response) {
        try (SqlSession session = MybatisConnection.getConnection()) {
            ProjectsDTO project = new ProjectsDTO();
            project.setProjectId(Integer.parseInt(request.getParameter("projectId")));
            project.setTitle(request.getParameter("title"));
            project.setDescription(request.getParameter("description"));
            project.setLeaderId(Integer.parseInt(request.getParameter("leaderId")));
            project.setRecruitStatus(ProjectsDTO.RecruitStatus.valueOf(request.getParameter("recruitStatus").toUpperCase()));

            ProjectsDAO dao = new ProjectsDAO(session);
            dao.update(project);
            session.commit();

            return "redirect:info?projectId=" + project.getProjectId();
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
}
