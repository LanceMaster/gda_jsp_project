<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/login" method="post">
		<div class="form-group">
			<label for="username">아이디</label> <input type="text"
				class="form-control" name="username" />
		</div>
		<div class="form-group">
			<label for="password">비밀번호</label> <input type="password"
				class="form-control" name="password" />
		</div>
		<button type="submit" class="btn btn-primary btn-block">로그인</button>
	</form>
</body>
</html>