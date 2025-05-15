package controller;

import model.dao.ProgressDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 📈 진도율 저장 Ajax 컨트롤러
 */
@WebServlet("/lecture/saveProgress")
public class ProgressAjaxController extends HttpServlet {

    private final ProgressDAO progressDAO = new ProgressDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));
        int contentId = Integer.parseInt(request.getParameter("contentId"));
        int progress = Integer.parseInt(request.getParameter("progress"));

        // MERGE 방식 저장 (없으면 insert, 있으면 update)
        progressDAO.saveOrUpdateProgress(userId, contentId, progress);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
