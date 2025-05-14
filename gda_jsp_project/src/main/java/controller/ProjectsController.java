package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import gdu.mskim.MSLogin;
import model.dao.ProjectsDAO;
import model.dto.ProjectsDTO;
import java.util.List;

@WebServlet(urlPatterns = {"/projects/*"}, initParams = {@WebInitParam(name = "view", value = "/view/")})
public class ProjectsController extends MskimRequestMapping {

    public String writerCheck(HttpServletRequest request, HttpServletResponse response) {
        String loginUser = (String) request.getSession().getAttribute("login");
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        ProjectsDAO dao = new ProjectsDAO();
        ProjectsDTO project = dao.listById(projectId);
        dao.close();

        if (loginUser == null || !loginUser.equals(String.valueOf(project.getLeaderId()))) {
            request.setAttribute("msg", "작성자 본인만 수정/삭제할 수 있습니다.");
            request.setAttribute("url", request.getContextPath() + "/projects/info?projectId=" + projectId);
            return "alert";
        }
        return null;
    }

    @RequestMapping("list")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        ProjectsDAO dao = new ProjectsDAO();
        List<ProjectsDTO> list = dao.listAll();
        dao.close();
        request.setAttribute("projects", list);
        return "projects/projectslist";
    }

    @RequestMapping("info")
    public String info(HttpServletRequest request, HttpServletResponse response) {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        ProjectsDAO dao = new ProjectsDAO();
        ProjectsDTO project = dao.listById(projectId);
        dao.close();
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
        ProjectsDAO dao = new ProjectsDAO();
        dao.insert(project);
        dao.close();
        return "redirect:list";
    }

    @RequestMapping("updateForm")
    @MSLogin("writerCheck")
    public String updateForm(HttpServletRequest request, HttpServletResponse response) {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        ProjectsDAO dao = new ProjectsDAO();
        ProjectsDTO project = dao.listById(projectId);
        dao.close();
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
        ProjectsDAO dao = new ProjectsDAO();
        dao.update(project);
        dao.close();
        return "redirect:info?projectId=" + project.getProjectId();
    }

    @RequestMapping("delete")
    @MSLogin("writerCheck")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        ProjectsDAO dao = new ProjectsDAO();
        dao.delete(projectId);
        dao.close();
        return "redirect:list";
    }
}
