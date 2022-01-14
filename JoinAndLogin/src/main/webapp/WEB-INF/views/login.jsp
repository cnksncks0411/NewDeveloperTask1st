<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
	// 로그인 성공 여부
	$(function(){
		if(${result==1}){
			alert("로그인에 성공하였습니다!");
			location.href="/index";
		}else if(${result==0}){
			alert("아이디 혹은 비밀번호를 다시 확인해주세요!");
			$("#id").val("");
			$("#pw").val("");
		}
	});
</script>
</head>
<body>
	<form action="/doLogin" method="post">
		<table>
			<tr>
				<td>아이디 : </td>
				<td><input type="text" name="id" id="id"></td>
			</tr>
			<tr>
				<td>비밀번호 : </td>
				<td><input type="password" name="pw" id="pw"></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="autoLogin" id="autoLogin"><label for="autoLogin">자동 로그인</label></td>
				<td><input type="submit" value="로그인"></td>
			</tr>
		</table>
	</form>
</body>
</html>