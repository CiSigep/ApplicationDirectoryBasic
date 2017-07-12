<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ra" uri="/resources/taglibs/roletag.tld" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/ApplicationDirectoryBasic/resources/CSS/default.css" rel="stylesheet" type="text/css">
<title>Home</title>
</head>
<body>
<%@ include file="/resources/Page/headerpiece.jsp" %>
	<div class="block">
		<ra:roleAccess roleRequired="debug_user">You are the debug user.<%-- Remove this for deployment --%></ra:roleAccess>
		<ra:roleAccess roleRequired="job_user">You are a job user.<%-- TODO: Add Job user page --%></ra:roleAccess>
		<ra:roleAccess roleRequired="comp_user"><jsp:include page="/resources/Page/CompUser.jsp"></jsp:include></ra:roleAccess>
		<ra:roleAccess roleRequired="comp_admin">You are a Company Admin. <%-- TODO: Determine if I should keep this role, and add a page if I do --%></ra:roleAccess>
		<ra:roleAccess roleRequired="role_admin"><jsp:include page="/resources/Page/RoleAdmin.jsp"></jsp:include></ra:roleAccess>
	</div>
</body>
</html>