package controller;

import java.io.*;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.dao.AdminDAO;
import model.dto.UserDTO;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AdminDAO adminDAO = new AdminDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain; charset=UTF-8");

		String action = request.getParameter("action");

		System.out.println("=== Request Parameters ===");
		request.getParameterMap().forEach((key, value) -> {
			System.out.println(key + ": " + String.join(", ", value));
		});
		System.out.println("=========================");

		if ("getUserDetail".equals(action)) {
			handleGetUserDetail(request, response);
		} else if ("deleteUser".equals(action)) {
			handleDeleteUser(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Invalid POST action");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if ("downloadResume".equals(action)) {
			handleDownloadResume(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Invalid GET action");
		}
	}

	private void handleGetUserDetail(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		System.out.println("userId: " + userId);

		UserDTO user = adminDAO.getUserById(userId);
		System.out.println("user: " + user);

		if (user != null) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String json = gson.toJson(user);
			response.setContentType("application/json; charset=UTF-8");
			try {
				response.getWriter().write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.getWriter().write("User not found");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		System.out.println("userId: " + userId);

		UserDTO user = adminDAO.getUserById(userId);
		System.out.println("user: " + user);

		if (user != null) {
			boolean deleted = adminDAO.deleteUser(userId);
			if (deleted) {
				response.setStatus(HttpServletResponse.SC_OK);
				try {
					response.getWriter().write("User deleted successfully");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				try {
					response.getWriter().write("Failed to delete user");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.getWriter().write("User not found");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleDownloadResume(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName = request.getParameter("fileName");

		if (fileName == null || fileName.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file name");
			return;
		}

		String uploadPath = getServletContext().getRealPath("/upload/resume");
		File file = new File(uploadPath, fileName);

		if (!file.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resume file not found");
			return;
		}

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
		response.setContentLengthLong(file.length());

		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
			 BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {

			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
	}
}
