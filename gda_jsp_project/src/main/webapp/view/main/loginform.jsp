<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>

<div>
  <div class="popup-close-btn">&times;</div>
  <h5 class="mb-3">로그인</h5>
  <form action="${pageContext.request.contextPath}/main/login" method="post">
    <div class="form-group">
      <label>아이디</label>
      <input type="text" name="username" class="form-control" />
    </div>
    <div class="form-group">
      <label>비밀번호</label>
      <input type="password" name="password" class="form-control" />
    </div>
    <button type="submit" class="btn btn-primary btn-block">로그인</button>
  </form>
</div>

</html>