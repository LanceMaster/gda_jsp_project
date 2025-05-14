package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import gdu.mskim.MSLogin;
import model.dao.ProjectsDAO;
import model.dto.ProjectsDTO;
import org.apache.ibatis.session.SqlSession;
import utils.MybatisConnection;

import java.util.List;

@WebServlet(urlPatterns = {"/projects/*"}, initParams = {@WebInitParam(name = "view", value = "/view/")})
public class ProjectsController extends MskimRequestMapping {

    public String writerCheck(HttpServletRequest request, HttpServletResponse response) {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        SqlSession session = MybatisConnection.getConnection();
        ProjectsDAO dao = new ProjectsDAO(session);
        ProjectsDTO project = dao.listById(projectId);
        MybatisConnection.close(session);

        String loginUser = (String) request.getSession().getAttribute("login");
        if (loginUser == null || !loginUser.equals(String.valueOf(project.getLeaderId()))) {
            request.setAttribute("msg", "작성자 본인만 수정/삭제할 수 있습니다.");
            request.setAttribute("url", request.getContextPath() + "/projects/info?projectId=" + projectId);
            return "alert";
        }
        return null;
    }
    @RequestMapping("projectslist")
    public String redirectToListWithDefaultParams(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:list?keyword=&sort=recent&status=";
    }
    @RequestMapping("list")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        int pageNum = 1;
        int pageSize = 10;
        if (request.getParameter("pageNum") != null) {
            pageNum = Integer.parseInt(request.getParameter("pageNum"));
        }

        SqlSession session = MybatisConnection.getConnection();
        ProjectsDAO dao = new ProjectsDAO(session);

        try {
            // ✅ 페이징 시작
            PageHelper.startPage(pageNum, pageSize);

            // ✅ 반드시 페이징 시작 후 즉시 select
            List<ProjectsDTO> list = dao.listAll();

            // ✅ 즉시 PageInfo로 감싸기
            PageInfo<ProjectsDTO> pageInfo = new PageInfo<>(list);
 
            // ✅ JSP로 전달
            request.setAttribute("projects", pageInfo.getList());
            request.setAttribute("pageInfo", pageInfo);

        } finally {
            MybatisConnection.close(session);
        }

        return "projects/projectslist";
    }

    @RequestMapping("info")
    public String info(HttpServletRequest request, HttpServletResponse response) {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        SqlSession session = MybatisConnection.getConnection();
        ProjectsDAO dao = new ProjectsDAO(session);
        ProjectsDTO project = dao.listById(projectId);
        MybatisConnection.close(session);

        request.setAttribute("project", project);
        return "projects/info";
    }

    @RequestMapping("writeForm")
    @MSLogin("writerCheck")
    public String writeForm(HttpServletRequest request, HttpServletResponse response) {
        return "projects/writeForm";
    }

    @RequestMapping("write")
    @MSLogin("writerCheck")
    public String write(HttpServletRequest request, HttpServletResponse response) {
        ProjectsDTO project = new ProjectsDTO();
        project.setTitle(request.getParameter("title"));
        project.setDescription(request.getParameter("description"));
        project.setThumbnail(request.getParameter("thumbnail"));
        project.setLeaderId(Integer.parseInt(request.getParameter("leaderId")));

        SqlSession session = MybatisConnection.getConnection();
        ProjectsDAO dao = new ProjectsDAO(session);
        dao.insert(project);
        MybatisConnection.close(session);

        return "redirect:list";
    }

    @RequestMapping("updateForm")
    @MSLogin("writerCheck")
    public String updateForm(HttpServletRequest request, HttpServletResponse response) {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        SqlSession session = MybatisConnection.getConnection();
        ProjectsDAO dao = new ProjectsDAO(session);
        ProjectsDTO project = dao.listById(projectId);
        MybatisConnection.close(session);

        request.setAttribute("project", project);
        return "projects/updateForm";
    }

    @RequestMapping("update")
    @MSLogin("writerCheck")
    public String update(HttpServletRequest request, HttpServletResponse response) {
        ProjectsDTO project = new ProjectsDTO();
        project.setProjectId(Integer.parseInt(request.getParameter("projectId")));
        project.setTitle(request.getParameter("title"));
        project.setDescription(request.getParameter("description"));
        project.setThumbnail(request.getParameter("thumbnail"));
        project.setLeaderId(Integer.parseInt(request.getParameter("leaderId")));
        project.setRecruitStatus(ProjectsDTO.RecruitStatus.valueOf(request.getParameter("recruitStatus").toUpperCase()));

        SqlSession session = MybatisConnection.getConnection();
        ProjectsDAO dao = new ProjectsDAO(session);
        dao.update(project);
        MybatisConnection.close(session);

        return "redirect:info?projectId=" + project.getProjectId();
    }

    @RequestMapping("delete")
    @MSLogin("writerCheck")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        SqlSession session = MybatisConnection.getConnection();
        ProjectsDAO dao = new ProjectsDAO(session);
        dao.delete(projectId);
        MybatisConnection.close(session);

        return "redirect:list";
    }
}
