package controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oreilly.servlet.MultipartRequest;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.dao.AdminDAO;
import model.dao.UserDAO;
import model.dto.UserDTO;

@WebServlet(urlPatterns = { "/admin/*" }, initParams = { @WebInitParam(name = "view", value = "/view/") })
public class AdminController extends MskimRequestMapping {

	public AdminDAO adminDAO = new AdminDAO();

	@RequestMapping("userlist")
	public String userlist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사용자 목록을 가져와서 JSP 페이지에 전달

		int pageNum = 1;
		if (request.getParameter("pageNum") != null) {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		int pageSize = 10;
		
		//keyword
		String keyword = request.getParameter("keyword");
		

		// 1. PageHelper로 페이징 시작
		PageHelper.startPage(pageNum, pageSize);

		 List<UserDTO> userList;
		    if (keyword != null && !keyword.trim().isEmpty()) {
		        // 이름 기준 검색
		        userList = adminDAO.searchUsersByName(keyword);
		        request.setAttribute("keyword", keyword); // 검색어 유지용
		    } else {
		        // 전체 목록
		        userList = adminDAO.userList();
		    }
		    
		    // 2. PageInfo로 페이징 정보 가져오기
		    PageInfo<UserDTO> pageInfo = new PageInfo<>(userList);
		    request.setAttribute("pageInfo", pageInfo);
		    request.setAttribute("userList", userList);
		
		return "/admin/userlist";

	}

}
