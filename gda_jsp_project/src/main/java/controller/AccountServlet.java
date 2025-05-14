package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.dao.UserDAO;

@WebServlet("/AccountServlet")
public class AccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 예시 DAO (실제 프로젝트에 맞게 import/생성)
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("emailDupCheck".equals(action)) {
            String email = request.getParameter("email");
            // 이메일 중복확인: 0이면 사용 가능, 1 이상이면 이미 사용 중
            int result = userDAO.emailDupCheck(email);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write(result == 0 ? "available" : "unavailable");
            return;
        }

        // ...다른 action 처리...
    }

    // 필요하다면 doGet도 구현
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}