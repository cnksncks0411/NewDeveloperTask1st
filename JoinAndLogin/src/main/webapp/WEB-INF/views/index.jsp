<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인페이지</title>

</head>
<body>
	<h2>메인페이지</h2>
	<!-- 세션에 로그인 정보에 따라 다르게 보여짐 -->
	<c:if test="${login==null }">
		<h3>로그인을 해주세요 (계정이 없다면 회원가입을 해주세요)</h3>
		<a href="/join"><input type="button" value="회원가입"></a>
		<a href="/login"><input type="button" value="로그인"></a>
	</c:if>
	<c:if test="${login!=null }">
		<h3>${login.name }님 안녕하세요!</h3>
		<a href="/logout"><input type="button" value="로그아웃" id="logout"></a>
	</c:if>
</body>
</html>