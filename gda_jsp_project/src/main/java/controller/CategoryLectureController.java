// 📁 controller.CategoryLectureController.java
package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/lecture/category")
public class CategoryLectureController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ✅ category 파라미터 추출
        String category = req.getParameter("category");

        // ✅ 기존 파라미터 유지 (keyword, sort, page 등)
        String keyword = req.getParameter("keyword");
        String sort = req.getParameter("sort");
        String page = req.getParameter("page");

        // ✅ redirect URL 구성
        StringBuilder redirectUrl = new StringBuilder("/lecture/lecturelist?");

        if (category != null && !category.isBlank()) {
            redirectUrl.append("category=").append(URLEncoder.encode(category, "UTF-8"));
        }

        if (keyword != null && !keyword.isBlank()) {
            redirectUrl.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8"));
        }

        if (sort != null && !sort.isBlank()) {
            redirectUrl.append("&sort=").append(URLEncoder.encode(sort, "UTF-8"));
        }

        if (page != null && !page.isBlank()) {
            redirectUrl.append("&page=").append(URLEncoder.encode(page, "UTF-8"));
        }

        // ✅ 최종 리디렉션
        resp.sendRedirect(redirectUrl.toString());
    }
}
