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
		if(${result } > 0){
			alert("회원가입에 성공했습니다!");
			location.href="/index";
		}else if(${result }==0){
			alert("서버 오류로 회원가입에 실패했습니다! 다시 시도해주세요!");
		}
	});	// index로 이동 끝
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
			}else if(patternId.test($("#id").val())!=true){
				alert("유효하지 않은 아이디입니다.");
				$("#id").val("");
			}else{
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
	// 패스워드 일치 확인
	$(function() {
		$("#pw2").keyup(function() {
			var patternPw = /^[a-zA-Z0-9]{4,15}$/;

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
			var patternName = /^[가-힣]{2,10}$/;
			var patternPhoneNumber = /^[0]{1}[0-9]{1,2}[0-9]{8}$/;
			var patternEmail1 = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*$/;
			var patternEmail2 = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;
			
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
			}else if(patternPhoneNumber.test($("#phone").val()) != true){
				alert("* 전화번호를 확인 해주세요.");
				$("#phone").val("");
				$("#phone").focus();
				return false;
			}else if(patternEmail1.test($("#email").val()) != true){
				alert("이메일 앞 부분이 잘못 입력 되었습니다. 다시 입력 해주세요.");
				$("#email").val("");
				$("#email").focus();
				return false;
			}else if (patternEmail2.test($("#email2").val()) != true) {
				alert("이메일 뒷 부분이 잘못 입력 되었습니다. 다시 입력 해주세요.");
				return false;
			}else if($("#addr").val()==""){
				alert("주소를 입력하지 않으셨습니다.");
				return false;
			}
		});
	}); // 정규 표현식 사용 끝

	// 이메일 select box
	$(function() {
		var $sel = $("#emailList");
		var $email = $("#email");
		var $etype = $("#email2");

		$sel.change(function() {
			val = $sel.val();
			if (val.length < 1) {
				$etype.removeAttr("readonly");
			} else {
				$etype.attr("readonly", true);
			}
			$etype.val(val);
		});
	}); // 이메일 select box 끝
</script>
<style type="text/css">
.title {
	font-weight: bold;
}

.notice {
	font-size: 11px;
	color: gray;
}
</style>
</head>
<body>
	<form action="/doJoin" method="post" name="dojoin" id="join">
		<table>
			<tr>
				<td class="title">아이디* :</td>
				<td><input type="text" name="id" id="id" maxlength="16"></td>
				<td><input type="button" value="중복확인" id="idCheck"></td>
				<td><input type="hidden" id="clickChk" value="no"/></td>
			</tr>
			<tr>
				<td colspan="3" class="notice">※ 첫 글자는 영문으로 4~16자 까지 가능, 영문,숫자만 사용 가능</td>
			</tr>
			<tr>
				<td class="title">비밀번호* :</td>
				<td><input type="password" name="pw" id="pw" maxlength="15" /></td>
			</tr>
			<tr>
				<td colspan="3" class="notice">※ 영문/숫자 혼용으로 4~15자 까지 가능</td>
			</tr>
			<tr>
				<td class="title">비밀번호 확인* :</td>
				<td><input type="password" id="pw2" maxlength="15" /></td>
			</tr>
			<tr>
				<td colspan="3" id="pwtxt"><td>
			</tr>
			<tr>
				<td class="title">이름* :</td>
				<td><input type="text" name="name" id="name" maxlength="10"></td>
			</tr>
			<tr>
				<td class="title">전화번호* :</td>
				<td><input type="text" name="phone" id="phone" maxlength="11" placeholder="'-'를 제외하고 입력해주세요" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" /></td>
			</tr>
			<tr>
				<td class="title">이메일* :</td>
				<td><input type="text" name="email" id="email" maxlength="20"/>@<input type="text" name="email2" id="email2" /> <select id="emailList" name="emailList">
						<option value="" selected="selected">::직접입력::</option>
						<option value="naver.com">네이버</option>
						<option value="daum.net">다음</option>
						<option value="hanmail.net">한메일</option>
						<option value="nate.com">네이트</option>
						<option value="gmail.com">구글</option>
				</select></td>
			</tr>
			<tr>
				<td class="title">주소* :</td>
				<td><input type="text" id="zipcode" name="zipcode" readonly /><a
					href="javascript:;" onclick="zipSearch()"><button type="button">우편번호 찾기</button></a></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="text" id="addr" name="addr" readonly /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="text" id="addr2" name="addr" placeholder="상세주소를 적어주세요" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="회원가입" id="joinBtn"/></td>
			</tr>
		</table>
	</form>
</body>
</html>