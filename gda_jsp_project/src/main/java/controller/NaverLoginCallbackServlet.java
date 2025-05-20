package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.dao.NaverUserDAO;
import model.dto.NaverUserDTO;
import model.dto.UserDTO;

/**
 * Servlet implementation class NaverLoginCallbackServlet
 */
@WebServlet("/login/oauth2/code/naver")
public class NaverLoginCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String CLIENT_ID = "13FTrHiwK64owp3t5Y4X"; // 네이버 애플리케이션 클라이언트 ID
	private static final String CLIENT_SECRET = "t_X_S09Ovq"; // 네이버 애플리케이션 시크릿
	private static final String CALLBACK_URL = "http://localhost:8080/login/oauth2/code/naver";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 네이버에서 전달받은 인증 코드와 상태 값
		String code = request.getParameter("code");
		String state = request.getParameter("state");

		String tokenUrl = "https://nid.naver.com/oauth2.0/token" + "?grant_type=authorization_code" + "&client_id="
				+ CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + code + "&state=" + state;

		// Access Token 요청
		// 네이버에 토큰 요청
		URL url = new URL(tokenUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		BufferedReader br;

		if (responseCode == 200) { // 정상 응답
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} else { // 에러 응답
			br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}

		String inputLine;
		StringBuffer tokenResponse = new StringBuffer();
		while ((inputLine = br.readLine()) != null) {
			tokenResponse.append(inputLine);
		}
		br.close();

		JsonObject jsonObject = new Gson().fromJson(tokenResponse.toString(), JsonObject.class);
		String accessToken = jsonObject.get("access_token").getAsString();

		String apiURL = "https://openapi.naver.com/v1/nid/me";
		URL userInfoUrl = new URL(apiURL);
		HttpURLConnection userInfoCon = (HttpURLConnection) userInfoUrl.openConnection();
		userInfoCon.setRequestMethod("GET");
		userInfoCon.setRequestProperty("Authorization", "Bearer " + accessToken);

		responseCode = userInfoCon.getResponseCode();

		if (responseCode == 200) { // 정상 응답
			br = new BufferedReader(new InputStreamReader(userInfoCon.getInputStream()));
		} else { // 에러 응답
			br = new BufferedReader(new InputStreamReader(userInfoCon.getErrorStream()));
		}

		StringBuffer userInfoResponse = new StringBuffer();
		while ((inputLine = br.readLine()) != null) {
			userInfoResponse.append(inputLine);
		}
		br.close();

		// 네이버 사용자 정보 파싱
		JsonObject userInfo = new Gson().fromJson(userInfoResponse.toString(), JsonObject.class);
		JsonObject response1 = userInfo.getAsJsonObject("response");

		// 사용자 정보 추출
		String socialId = response1.get("id").getAsString();
		String email = response1.has("email") ? response1.get("email").getAsString() : "";
		String name = response1.has("name") ? response1.get("name").getAsString() : "";
		String nickname = response1.has("nickname") ? response1.get("nickname").getAsString() : "";
		String profileImage = response1.has("profile_image") ? response1.get("profile_image").getAsString() : "";
		String birthyear = response1.get("birthyear").getAsString();
		String birthday = response1.get("birthday").getAsString();
		String mobile = response1.get("mobile").getAsString();

		String fullBirthdateStr = birthyear + "-" + birthday; // "1999-11-03"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate birthdate = LocalDate.parse(fullBirthdateStr, formatter);
		Date birthDate = Date.from(birthdate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		System.out.println(socialId);
		System.out.println(email);
		System.out.println(name);
		System.out.println(nickname);
		System.out.println(profileImage);
		System.out.println(birthDate);
		System.out.println(mobile);

		// 사용자 정보를 세션에 저장
		/*
		 * request.getSession().setAttribute("naverId", naverId);
		 * request.getSession().setAttribute("email", email);
		 * request.getSession().setAttribute("name", name);
		 */

//		Map<String, Object> userInfoMap = new HashMap<>();
//		userInfoMap.put("socialId", socialId);
//		userInfoMap.put("email", email);
//		userInfoMap.put("name", name);
//		userInfoMap.put("nickname", nickname);
//		userInfoMap.put("profileImage", profileImage);
//		userInfoMap.put("socialType", "naver");

		NaverUserDTO naverUserDTO = NaverUserDAO.findBySocialId(socialId, "NAVER");

		if (naverUserDTO == null) {
			// 신규 사용자 등록
			NaverUserDTO newUser = new NaverUserDTO();
			newUser.setSocialId(socialId);
			newUser.setSocialType("NAVER");
			newUser.setEmail(email);
			newUser.setName(name);
			newUser.setNickname(nickname);
			newUser.setProfileImage(profileImage);
			// DB에 신규 사용자 등록 로직 추가
			int result = NaverUserDAO.insertNewUser(newUser);

			if (result > 0) {
				naverUserDTO = NaverUserDAO.findBySocialId(socialId, "NAVER");
			} else {
				throw new ServletException("소셜 사용자 등록 실패");

			}

		}

		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(email);
		userDTO.setName(name);
		userDTO.setBirthdate(birthDate); // 필드가 Date일 경우
		userDTO.setPhone(mobile);

		request.getSession().setAttribute("user", userDTO);

		// 로그인 성공 후 리다이렉트할 URL
		response.sendRedirect(request.getContextPath() + "/user/mainpage");

	}

}
