<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript">
	// 회원가입 성공 시 index로 이동
	$(function(){
		if(${result } == 1){
			alert("회원가입에 성공했습니다!");
			location.href="./main";
		}else if(${result } == 0){
			alert("서버 오류로 회원가입에 실패했습니다! 다시 시도해주세요!");
		}
	}); // index로 이동 끝
</script>
<script type="text/javascript">
	// daum 주소 API 사용
	function zipSearch(){
	    new daum.Postcode({
	        oncomplete: function(data) {
	            $("#zipcode").val(data.zonecode);
	            $("#addr").val(data.address);
	            $("#addr2").focus();
	        }
	  }).open(); 
	} // daum 주소 API 사용 끝
</script>
<script type="text/javascript">
	// id 중복확인 ajax
	$(function(){
		$("#idCheck").click(function(){
			// ID 정규표현식 사용
			var patternId = /^[a-zA-Z]{1}[a-zA-Z0-9]{3,15}$/;
			
			if($("#id").val()==""){
				alert("아이디를 입력해주세요.");
			}/*else if(patternId.test($("#id").val()) != true){
				alert("사용할 수 없는 아이디입니다.");
				$("#id").val("");
			}*/else{
				$.ajax({
					url:"./checkId",
					type:"get",
					data:{
						"id":$("#id").val()
					},
					success:function(data){
						if(data>0){
							alert("중복된 아이디입니다.");
							$("#id").val("");
						}else{
							alert("사용 가능한 아이디입니다.");
							$("#pw").focus();
							$("#clickChk").val("ok");
						}
					},
					error:function(textStatus, errorThrown){
						alert("실패 : "+textStatus);
						alert(errorThrown);
					}
				});
			}
		});
	});	// id 중복확인 끝
</script>
<script type="text/javascript">
	$(function(){
		$("#id").keyup(function() {
			var patternId = /^[a-zA-Z]{1}[a-zA-Z0-9]{3,15}$/;
			
			if(patternId.test($("#id").val()) != true) {
				$("#idtxt").css("color", "red");
				$("#idtxt").css("font-size", "11px");
				$("#idtxt").css("font-weight", "bold");
				$("#idtxt").text("아이디는 영문자로 시작하여 4~16자의 영문, 숫자 조합이어야 합니다.");
			}else{
				$("#idtxt").text("");
			}
		});
	});
</script>
<script type="text/javascript">
	// 패스워드 일치 확인
	$(function() {
		$("#pw2").keyup(function() {
			var patternPw = /^[a-zA-Z0-9]{8,16}$/;

			if ($("#pw").val() != $("#pw2").val()) {
				$("#pwtxt").css("color", "red");
				$("#pwtxt").css("font-size", "11px");
				$("#pwtxt").css("font-weight", "bold");
				$("#pwtxt").text("* 비밀번호가 일치하지 않습니다. 다시 입력하세요.");
				return false;
			} else if (patternPw.test($("#pw").val()) != true) {
				$("#pwtxt").css("color", "red");
				$("#pwtxt").css("font-size", "11px");
				$("#pwtxt").css("font-weight", "bold");
				$("#pwtxt").text("* 비밀번호를 올바르게 입력하지 않으셨습니다.");
				return false;
			} else {
				$("#pwtxt").css("color", "green");
				$("#pwtxt").css("font-size", "11px");
				$("#pwtxt").css("font-weight", "bold");
				$("#pwtxt").text("* 비밀번호를 올바르게 입력하셨습니다.");
			}
		});
	}); // 패스워드 일치 확인 끝
	
	// 정규표현식 사용
	$(function() {
		$("#joinBtn").click(function() {
			var patternName = /^[가-힣]{2,8}$/;
			
			if($("#clickChk").val()=="no"){
				alert("아이디 중복확인을 해주세요.");
				return false;
			}else if($("#pw").val()==""){
				alert("비밀번호를 입력해주세요.");
				return false;
			}else if($("#pw").val() != $("#pw2").val()){
				alert("비밀번호를 확인해주세요.");
				return false;
			}else if(patternName.test($("#name").val()) != true){
				alert("* 이름은 한글로만 입력 해주세요.");
				$("#name").val("");
				$("#name").focus();
				return false;
			}else if($("#level").val()==""){
				alert("회원등급을 선택 해주세요.");
				return false;
			}else if($("#regDate").val()==""){
				alert("생년월일을 입력 해주세요.");
				return false;
			}
		});
	}); // 정규 표현식 사용 끝

	// 회원등급 select box
	$(function() {
		var $sel = $("#levelList");
		var $level = $("#level");

		$sel.change(function() {
			val = $sel.val();
			if (val.length < 1) {
				$level.removeAttr("readonly");
			} else {
				$level.attr("readonly", true);
			}
			$level.val(val);
		});
	}); // 회원등급 select box 끝
</script>
<style type="text/css">
.title {
	font-weight: bold;
}

.notice {
	font-size: 11px;
	color: red;
}
</style>

</head>
<body>
	<form action="./signup" method="post" name="join" id="join">
		<table>
			<tr>
				<td class="title">아이디* :</td>
				<td><input type="text" name="id" id="id" maxlength="16" value="${userDto.id }"></td>
				<td><input type="button" value="중복확인" id="idCheck"></td>
				<td><input type="hidden" id="clickChk" value="no"/></td>
			</tr>
			<tr>
				<td colspan="3" class="notice" id="idtxt">${valid_id }</td>
			</tr>
			<tr>
				<td class="title">비밀번호* :</td>
				<td><input type="password" name="pw" id="pw" maxlength="15" /></td>
			</tr>
			<tr>
				<td class="title">비밀번호 확인* :</td>
				<td><input type="password" id="pw2" maxlength="16" /></td>
			</tr>
			<tr>
				<td colspan="3" class="notice" id="pwtxt">${valid_pw }<td>
			</tr>
			<tr>
				<td class="title">이름* :</td>
				<td><input type="text" name="name" id="name" maxlength="8" value="${userDto.name }"></td>
			</tr>
			<tr>
				<td class="title">회원등급* :</td>
				<td><input type="text" name="level" id="level" value="${userDto.level }" readonly>
				<script>
				$(function(){
					$("#levelList").val("${userDto.level }").prop("selected",true);
				});
				</script>
					<select id="levelList" name="levelList">
						<option value="" selected="selected"></option>
						<option value="A">A등급</option>
						<option value="B">B등급</option>
						<option value="C">C등급</option>
						<option value="D">D등급</option>
					</select></td>
			</tr>
			<tr>
				<td class="title">자기소개 :</td>
				<td><textarea rows="10" cols="30" maxlength="256" name="desc" id="desc">${userDto.desc }</textarea></td>
			</tr>
			<tr>
				<td class="title">생년월일* :</td>
				<td><input type="datetime-local" name="regDate" id="regDate" value="${userDto.regDate }"></td>
			</tr>
			<tr>
				<td><input type="submit" value="회원가입" id="joinBtn"/></td>
			</tr>
		</table>
	</form>
</body>
</html>