package filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginCheckFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		HttpSession session = request.getSession(false);

		boolean isLoggedIn = session != null && session.getAttribute("user") != null;
		String requestURI = request.getRequestURI();
		String queryString = request.getQueryString();

		boolean isLoginPage = requestURI.contains("/user/loginform") || requestURI.contains("/user/login");

		if (!isLoggedIn && !isLoginPage) {
			// 로그인 안 된 경우, 원래 URL 저장
			String fullURL = requestURI + (queryString != null ? "?" + queryString : "");
			request.getSession(true).setAttribute("redirectAfterLogin", fullURL);

			response.sendRedirect(request.getContextPath() + "/user/loginform");
			return;
		}

		chain.doFilter(request, response); // 로그인한 경우 그대로 진행
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}
}
