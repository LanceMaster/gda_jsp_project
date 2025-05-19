package controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.dao.UserDAO;

@WebServlet("/AccountServlet")
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserDAO userDAO = new UserDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain; charset=UTF-8");

		String action = request.getParameter("action");
		if (action == null) {
			response.getWriter().write("fail");
			return;
		}

		switch (action) {
		case "findId":
			handleFindId(request, response);
			break;
		case "findPassword":
			handleFindPassword(request, response);
			break;
		case "emailDupCheck":
			handleEmailDupCheck(request, response);
			break;
		case "sendVerificationEmail":
			handleSendVerificationEmail(request, response);
			break;
		case "checkAuthCode":
			handleCheckAuthCode(request, response);
			break;
		case "changePassword":
			handleChangePassword(request, response);
			break;
		case "deleteAccount":
			handleDeleteAccount(request, response);
			break;
		case "checkResume":
			handleCheckResume(request, response); // 이 메서드는 구현되지 않음
			break;
		default:
			response.getWriter().write("fail");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if ("findIdForm".equals(action)) {
			request.getRequestDispatcher("/user/findidform").forward(request, response);
		} else if ("downloadResume".equals(action)) {
			handleDownloadResume(request, response);
		} else {
			doPost(request, response);
		}

	}

	private void handleCheckResume(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		if (email == null || email.isEmpty()) {
			response.getWriter().write("fail");
			return;
		}
		String resumeFilename = userDAO.getResumeFilename(email);
		if (resumeFilename != null && !resumeFilename.isEmpty()) {
			String filePath = getServletContext().getRealPath("/upload/resume/" + resumeFilename);
			File file = new File(filePath);
			if (file.exists()) {
				response.getWriter().write("exist");
				return;
			}
		}
		response.getWriter().write("fail");
	}

	private void handleDownloadResume(HttpServletRequest request, HttpServletResponse response) {
		String email = request.getParameter("email");
		try {
			String resumeFilename = userDAO.getResumeFilename(email);
			if (resumeFilename != null && !resumeFilename.isEmpty()) {
				// 회원가입 때 저장된 경로와 일치하도록 수정
				String filePath = getServletContext().getRealPath("/upload/resume/" + resumeFilename);

				File file = new File(filePath);
				if (!file.exists()) {
					response.getWriter().write("fail");
					return;
				}

				// 파일 다운로드용 헤더 설정
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + resumeFilename + "\"");
				response.setContentLengthLong(file.length());

				// 파일 스트림을 읽어 response 출력
				try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
						ServletOutputStream out = response.getOutputStream()) {
					byte[] buffer = new byte[4096];
					int bytesRead = -1;
					while ((bytesRead = in.read(buffer)) != -1) {
						out.write(buffer, 0, bytesRead);
					}
					out.flush();
				}

			} else {
				response.getWriter().write("fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleFindId(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		String birth = request.getParameter("birth");

		if (name == null || name.isEmpty() || birth == null || birth.isEmpty()) {
			response.getWriter().write("fail");
			return;
		}

		String id = userDAO.findIdByNameAndBirth(name, birth); // DAO 메서드도 수정 필요
		response.getWriter().write(id != null ? id : "fail");
	}

	private void handleFindPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		String name = request.getParameter("name");

		if (email == null || email.isEmpty() || name == null || name.isEmpty()) {
			response.getWriter().write("fail");
			return;
		}

		boolean isValidUser = userDAO.findPassword(email, name);
		if (!isValidUser) {
			response.getWriter().write("fail");
			return;
		}

		String tempPassword = generateTemporaryPassword();
		boolean updated = userDAO.updatePasswordByEmail(email, tempPassword);
		if (!updated) {
			response.getWriter().write("fail");
			return;
		}

		boolean sent = sendTempPasswordByEmail(email, tempPassword);
		response.getWriter().write(sent ? "success" : "fail");
	}

	private void handleEmailDupCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		int result = userDAO.emailDupCheck(email);
		response.getWriter().write(result == 0 ? "available" : "unavailable");
	}

	private void handleSendVerificationEmail(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String email = request.getParameter("email");

		String authCode = String.format("%06d", new Random().nextInt(1000000));
		request.getSession().setAttribute("authcode", authCode);

		boolean sent = sendEmail(email, "회원가입 인증코드", "인증코드: " + authCode);
		response.getWriter().write(sent ? "sent" : "fail");
	}

	private void handleCheckAuthCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String inputCode = request.getParameter("authCode");
		String sessionCode = (String) request.getSession().getAttribute("authcode");
		response.getWriter().write(inputCode != null && inputCode.equals(sessionCode) ? "success" : "fail");
	}

	private void handleChangePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		String newPassword = request.getParameter("newPassword");

		if (email == null || newPassword == null || email.isEmpty() || newPassword.isEmpty()) {
			response.getWriter().write("fail");
			return;
		}

		boolean updated = userDAO.updatePasswordByEmail(email, newPassword);
		response.getWriter().write(updated ? "success" : "fail");
	}

	private void handleDeleteAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		boolean deleted = userDAO.deleteAccount(email);

		if (deleted) {
			request.getSession().invalidate();
		}
		response.getWriter().write(deleted ? "success" : "fail");
	}

	// 공통 메일 전송 메서드
	private boolean sendEmail(String to, String subject, String content) {
		String host = "smtp.gmail.com";
		String user = "ukirrer@gmail.com";
		String password = "lezh txfa esth gzoj"; // 앱 비밀번호

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

		try {
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(user));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setSubject(subject);
			msg.setText(content);
			Transport.send(msg);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean sendTempPasswordByEmail(String email, String tempPassword) {
		String content = "임시 비밀번호: " + tempPassword + "\n로그인 후 비밀번호를 꼭 변경해주세요.";
		return sendEmail(email, "임시 비밀번호 안내", content);
	}

	private String generateTemporaryPassword() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}
}
