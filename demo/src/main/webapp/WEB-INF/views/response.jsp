<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 업로드</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#result").click(function(){
		$.ajax({
			url:"./selectData",
			type:"post",
			contentType:"application/json; charset=utf-8;",
			datatype:"json",	/* json타입으로 받아오기 */
			success:function(data){
				/* 등록된 데이터 테이블 형식으로 가져오기 */
				var html = "<h3>User 테이블 전체 등록 데이터</h3>"
				html +="<table><tr><td>ID</td><td>PWD</td><td>NAME</td><td>LEVEL</td><td>DESC</td><td>REG_DATE</td></tr>";
				for(key in data){
					html+="<tr>";
					html+="<td>"+data[key].id+"</td>";
					html+="<td>"+data[key].pwd+"</td>";
					html+="<td>"+data[key].name+"</td>";
					html+="<td>"+data[key].level+"</td>";
					html+="<td>"+data[key].desc+"</td>";
					html+="<td>"+moment(data[key].reg_date).format('YYYY년MM월DD일 HH시mm분')+"</td>";
					html+="</tr>";
				}
				html+="</table>"
				$("#dataTable").append(html);
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
	table, td{
		border: 1px solid black;
		border-collapse: collapse;
	}
</style>
</head>
<body>
	<!-- 입력에 성공한 라인 수와 전체 라인 수 비교 -->
	<c:if test="${map.success==map.count }">
		<h2>레코드건수 : ${map.count }건 입력 성공</h2>
		<input type="button" value="레코드 조회" id="result">
		<div id="dataTable">
		</div>
	</c:if>

	<c:if test="${map.success!=map.count }">
		<h2>성공 : ${map.success }건, 실패 : ${map.count-map.success }</h2>
		<c:forEach items="${map.failList }" var="fail">
			<!-- fail[0]=lineNum fail[1]=lineTxt -->
			<p>실패한 라인 번호 : ${fail[0] }</p>
			<p>실패한 라인 텍스트 : ${fail[1] }</p>
		</c:forEach>
			
	</c:if>
</body>
</html>