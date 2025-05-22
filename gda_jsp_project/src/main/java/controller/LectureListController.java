// 📁 controller.LectureListController.java
package controller;

import model.dto.LectureSearchCondition;

import service.LectureService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet({"/lecture/lecturelist"}) 
public class LectureListController extends HttpServlet {
    private final LectureService lectureService = new LectureService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LectureSearchCondition cond = new LectureSearchCondition();
        String category = req.getParameter("category");
        String keyword = req.getParameter("keyword");
        String sort = req.getParameter("sort");
        
        cond.setCategory(category != null && !category.trim().isEmpty() ? category : null);
        cond.setKeyword((keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null);
        cond.setSort((sort != null && !sort.trim().isEmpty()) ? sort : "latest");
        cond.setPage(req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1);

        Map<String, Object> result = lectureService.getLecturePage(cond);

        req.setAttribute("lectures", result.get("lectures"));
        req.setAttribute("totalCount", result.get("totalCount"));
        req.setAttribute("page", cond.getPage());
        req.setAttribute("size", cond.getSize());
        req.setAttribute("param", req.getParameterMap());
        
        System.out.println("===== 강의 조회 결과 =====");
        for (Map.Entry<String, Object> entry : result.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("=========================");


        req.getRequestDispatcher("/view/lecture/lectureList.jsp").forward(req, resp);

    }
}
