<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/ApplicationDirectoryBasic/resources/CSS/default.css" rel="stylesheet" type="text/css">
<title>Login</title>
</head>
<body>
<%@ include file="/resources/Page/headerpiece.jsp" %>
	<div class="block">
		<div class="loginblock">
			<form action="/ApplicationDirectoryBasic/login" method="POST">
				<label for="username">Username: </label>
				<input id="username" type="text" name="user" />
				<br/>
				<label for="password">Password:</label>
				<input id="password" type="password" name="pass" />
				<br/>
				<input type="submit" value="Login" />
				
			</form>
			<c:if test="${not empty Error}"><br/><span class="error loginerror">${Error}</span></c:if>
		</div>
	</div>
</body>
</html>