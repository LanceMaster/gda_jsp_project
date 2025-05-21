package filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

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

            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('로그인이 필요한 서비스입니다. 로그인 페이지로 이동합니다.');");
            out.println("location.href='" + request.getContextPath() + "/user/loginform';");
            out.println("</script>");
            out.close();
            return;
        }

        chain.doFilter(request, response); // 로그인 되어 있으면 계속 진행
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
