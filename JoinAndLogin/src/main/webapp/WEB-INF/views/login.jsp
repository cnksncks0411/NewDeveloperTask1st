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
	});	// 로그인 성공 여부 끝
</script>
<script type="text/javascript">
	$(function(){
		getid();
		$("#saveId").click(function(){
			saveid();
		});
	});
	  
	function saveid() {
		var expdate = new Date();
		if($("#saveId").prop("checked")){
			expdate.setTime(expdate.getTime() + 1000 * 3600 * 24 * 30); // 30일
	   	}else {
	    	expdate.setTime(expdate.getTime() - 1);
	   	}
	   		setCookie("saveid", $("#id").val(), expdate);
	} //saveid()
	 
	function setCookie (name, value, expires) {
		document.cookie = name + "=" + escape (value) +"; path=/; expires=" + expires.toGMTString();
	} //setCookie(name,value,expires)

	function getCookie(Name) {
		var search = Name + "=";
		// 쿠키가 설정되어 있다면
	    if (document.cookie.length > 0) { 
	    	offset = document.cookie.indexOf(search);
	    	// 쿠키가 존재하면
	    	if (offset != -1) { 
		        offset += search.length;
		        // set index of beginning of value
		        end = document.cookie.indexOf(";", offset);
		        // 쿠키 값의 마지막 위치 인덱스 번호 설정
		        if (end == -1)
		        end = document.cookie.length;
		        return unescape(document.cookie.substring(offset, end));
			}
		}
	    return "";
	} //getCookie(Name)

	function getid() {
		var saveId = getCookie("saveid");
		if(saveId != "") {
			$("#id").val(saveId);
			$("#saveId").prop("checked",true);
		}
	} //getid()
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
				<td><input type="checkbox" name="saveId" id="saveId" onClick="javascript:saveid(document.new_user_session);"><label for="saveId">아이디 저장</label></td>
				<td><input type="checkbox" name="autoLogin" id="autoLogin"><label for="autoLogin">자동 로그인</label></td>
				<td><input type="submit" value="로그인"></td>
			</tr>
		</table>
	</form>
</body>
</html>