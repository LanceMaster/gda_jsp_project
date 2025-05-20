package controller;

import com.google.gson.*;
import model.dao.CheckoutDAO;
import model.dto.UserDTO;
import org.apache.ibatis.session.SqlSession;
import utils.IamportUtils;
import utils.MyBatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet(urlPatterns = { "/cart/checkout" }, initParams = { @WebInitParam(name = "view", value = "/view/") })
public class CheckoutController extends HttpServlet {

    private final CheckoutDAO checkoutDAO = new CheckoutDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            resp.getWriter().write("{\"success\": false, \"message\": \"로그인이 필요합니다.\"}");
            return;
        }

        // 1. JSON 요청 파싱
        BufferedReader reader = req.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        JsonObject json = JsonParser.parseString(jsonBuilder.toString()).getAsJsonObject();
        String impUid = json.get("imp_uid").getAsString();
        JsonArray lectureIdsJson = json.getAsJsonArray("lectureIds");
        JsonArray amountsJson = json.getAsJsonArray("amounts");

        if (lectureIdsJson.size() != amountsJson.size()) {
            resp.getWriter().write("{\"success\": false, \"message\": \"강의 정보와 결제 정보 불일치.\"}");
            return;
        }

        // 2. 총 결제 금액 계산
        int expectedAmount = 0;
        for (JsonElement amountElem : amountsJson) {
            expectedAmount += amountElem.getAsInt();
        }

        // 3. 아임포트 결제 검증
        boolean valid = IamportUtils.verifyPayment(impUid, expectedAmount);
        if (!valid) {
            resp.getWriter().write("{\"success\": false, \"message\": \"결제 검증 실패.\"}");
            return;
        }

        // 4. DB 처리
        try (SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            for (int i = 0; i < lectureIdsJson.size(); i++) {
                int lectureId = lectureIdsJson.get(i).getAsInt();
                int amount = amountsJson.get(i).getAsInt();

                Map<String, Object> param = new HashMap<>();
                param.put("userId", user.getUserId());
                param.put("lectureId", lectureId);
                param.put("amountPaid", amount);
                param.put("paymentMethod", "KAKAO");

                boolean enrolled = checkoutDAO.insertEnrollment(sqlSession, param);
                if (!enrolled) {
                    sqlSession.rollback();
                    resp.getWriter().write("{\"success\": false, \"message\": \"수강 등록 실패.\"}");
                    return;
                }

                boolean deleted = checkoutDAO.deleteCartItem(sqlSession, param);
                if (!deleted) {
                    sqlSession.rollback();
                    resp.getWriter().write("{\"success\": false, \"message\": \"장바구니 삭제 실패.\"}");
                    return;
                }
            }

            resp.getWriter().write("{\"success\": true}");
        }
    }
}
