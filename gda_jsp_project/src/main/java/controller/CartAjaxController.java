package controller;

import model.dao.CartDAO;
import model.dto.UserDTO;
import model.mapper.CartMapper;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import utils.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/cart/add")
public class CartAjaxController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null) {
            out.print("{\"success\": false, \"message\": \"로그인이 필요합니다.\"}");
            return;
        }

        // JSON 파싱
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        int lectureId = new JSONObject(sb.toString()).getInt("lectureId");
        int userId = user.getUserId();

        try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            CartMapper mapper = sqlSession.getMapper(CartMapper.class);
            
            if(mapper.existsInCart(userId, lectureId) > 0) {
                out.print("{\"success\": false, \"message\": \"이미 장바구니에 있는 강의입니다.\"}");
                return;
            }
            
            mapper.insertCart(userId, lectureId); // ✅ 실제 SQL 실행
            out.print("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

}
