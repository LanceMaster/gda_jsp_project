package controller;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.dao.UserDAO;

@WebServlet("/AccountServlet")
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// DAO 객체 생성 (프로젝트에 맞게 조정)
	private UserDAO userDAO = new UserDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 요청 인코딩 설정 (필요시)
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain; charset=UTF-8");

		String action = request.getParameter("action");

		// 디버깅용 파라미터 출력
		System.out.println("=== Request Parameters ===");
		request.getParameterMap().forEach((key, value) -> {
			System.out.println(key + ": " + String.join(", ", value));
		});
		System.out.println("=========================");

		if ("emailDupCheck".equals(action)) {
			handleEmailDupCheck(request, response);
			return;
		}

		if ("sendVerificationEmail".equals(action)) {
			sendVerificationEmail(request, response);
			return;
		}

		if ("checkAuthCode".equals(action)) {
			handleCheckAuthCode(request, response);
			return;
		}

		if ("changePassword".equals(action)) {
			handleChangePassword(request, response);
			return;
		}

		// 다른 action 처리 시 추가...
	}

	private void handleEmailDupCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		int result = userDAO.emailDupCheck(email); // 0 = 사용 가능, 1 이상 = 사용 중
		response.getWriter().write(result == 0 ? "available" : "unavailable");
	}

	private void sendVerificationEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");

		// 6자리 랜덤 인증코드 생성
		StringBuilder authCodeBuilder = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			authCodeBuilder.append(random.nextInt(10));
		}
		String authCode = authCodeBuilder.toString();

		// 세션에 저장
		request.getSession().setAttribute("authcode", authCode);

		// SMTP 설정
		String host = "smtp.gmail.com";
		String user = "ukirrer@gmail.com";
		String password = "lezh txfa esth gzoj"; // 실제 비밀번호/앱 비밀번호 사용

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", host);

		Session mailSession = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		boolean sent = false;
		try {
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(user));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject("회원가입 인증코드");
			msg.setText("인증코드: " + authCode);

			Transport.send(msg);
			sent = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.getWriter().write(sent ? "sent" : "fail");
	}

	private void handleCheckAuthCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String inputCode = request.getParameter("authCode");
		String sessionCode = (String) request.getSession().getAttribute("authcode");

		if (sessionCode != null && sessionCode.equals(inputCode)) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("fail");
		}
	}

	private void handleChangePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		String newPassword = request.getParameter("newPassword");

		if (email == null || email.isEmpty() || newPassword == null || newPassword.isEmpty()) {
			response.getWriter().write("fail");
			return;
		}

		try {
			boolean updated = userDAO.updatePasswordByEmail(email, newPassword);
			response.getWriter().write(updated ? "success" : "fail");
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("fail");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if ("findIdForm".equals(action)) {
			// JSP 조각 include
			request.getRequestDispatcher("/user/findidform").forward(request, response);
			return;
		}

		doPost(request, response); // POST 처리도 같이 하기 때문에 남겨둬
	}
}
