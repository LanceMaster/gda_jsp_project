package controller;

import model.dao.CartDAO;
import model.dto.UserDTO;
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
	    req.setCharacterEncoding("UTF-8"); // ✅ JSON 인코딩 처리
	    PrintWriter out = resp.getWriter();

	    HttpSession session = req.getSession();
	    UserDTO user = (UserDTO) session.getAttribute("user");

	    if (user == null) {
	        out.print("{\"success\": false, \"message\": \"로그인이 필요합니다.\"}");
	        return;
	    }

	    StringBuilder sb = new StringBuilder();
	    try (BufferedReader reader = req.getReader()) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	    } catch (IOException e) {
	        out.print("{\"success\": false, \"message\": \"요청을 읽는 중 오류 발생.\"}");
	        return;
	    }

	    int lectureId;
	    try {
	        JSONObject json = new JSONObject(sb.toString());
	        lectureId = json.getInt("lectureId"); // ✅ 존재 여부 검증도 가능
	    } catch (Exception e) {
	        out.print("{\"success\": false, \"message\": \"잘못된 JSON 형식입니다.\"}");
	        return;
	    }

	    int userId = user.getUserId();

	    try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
	        CartDAO cartDAO = new CartDAO(sqlSession);
	        cartDAO.addToCart(userId, lectureId);
	        out.print("{\"success\": true}");
	    } catch (Exception e) {
	        out.print("{\"success\": false, \"message\": \"오류 발생: " + e.getMessage().replace("\"", "'") + "\"}");
	    }
	}

}
