<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인페이지</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#search").click(function(){
		$("#dataTable").text("");
		$("#dataTable *").remove();
		$.ajax({
			url:"./search",
			type:"get",
			data:{
				"category":$("#category").val(),
				"keyword":$("#keyword").val()
			},
			success:function(data){
				if(data==null){
					alert("검색 결과가 존재하지 않습니다.");
				}else{
					var html = "검색 결과 테이블"
					html +="<table><tr><th>ID</th><th>PW</th><th>NAME</th><th>LEVEL</th><th>DESC</th><th>REG_DATE</th></tr>";
					
					for(key in data){
						html+="<tr>";
						html+="<td>"+data[key].id+"</td>";
						html+="<td>"+data[key].pw+"</td>";
						html+="<td>"+data[key].name+"</td>";
						html+="<td>"+data[key].level+"</td>";
						html+="<td>"+data[key].desc+"</td>";
						html+="<td>"+data[key].regdate+"</td>";
						html+="</tr>";
					}
					
					html+="</table>"
					$("#dataTable").append(html);
				}
				
			},
			error:function(textStatus, errorThrown){
				alert("실패 : "+textStatus);
				alert(errorThrown);
			}
		});
	});
});
</script>
<style type="text/css">
table, td, th{
	border:1px solid black;
	border-collapse: collapse;
}
th, td{
	padding:0px 10px;
}
</style>
</head>
<body>
	<h2>메인페이지</h2>
	<!-- 세션에 로그인 정보에 따라 다르게 보여짐 -->
	<c:if test="${login==null }">
		<h3>로그인을 해주세요 (계정이 없다면 회원가입을 해주세요)</h3>
		<a href="./signup"><input type="button" value="회원가입"></a>
		<a href="./signin"><input type="button" value="로그인"></a>
	</c:if>
	<c:if test="${login!=null }">
		<h3>${login.name }님 안녕하세요!</h3>
		<a href="./signout"><input type="button" value="로그아웃" id="logout"></a>
	</c:if>
	<c:if test="${login.id=='asd123' }">
		<br/>
		<select name="category" id="category">
			<option value="id">아이디</option>
			<option value="name">이름</option>
			<option value="level">레벨</option>
			<option value="desc">자기소개</option>
		</select>
		<input type="text" name="keyword" id="keyword">
		<input type="button" value="제출" id="search">
		<div id="dataTable">
		</div>
	</c:if>
</body>
</html>