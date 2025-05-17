// 📁 controller.LectureListController.java
package controller;

import model.dto.LectureSearchCondition;

import service.LectureService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/lecture/category")
public class CategoryLectureController extends HttpServlet {
    private final LectureService lectureService = new LectureService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        // 🔴 기존: name 으로 받던 거 수정
        String category = req.getParameter("category");

        LectureSearchCondition cond = new LectureSearchCondition();
        cond.setCategory(category);
        cond.setPage(1);
        cond.setSort("latest");

        Map<String, Object> result = lectureService.getLecturePage(cond);

        req.setAttribute("lectures", result.get("lectures"));
        req.setAttribute("totalCount", result.get("totalCount"));
        req.setAttribute("page", cond.getPage());
        req.setAttribute("size", cond.getSize());
        req.setAttribute("param", req.getParameterMap());

        req.getRequestDispatcher("/view/lecture/lectureList.jsp").forward(req, resp);
    }
}
