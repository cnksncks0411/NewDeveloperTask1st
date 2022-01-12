<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 업로드</title>
<script type="text/javascript">

function checkFile(f){
	/* 비교 할 확장자 명 추출 */
	var dot = f.value.lastIndexOf('.');
	var fileType = f.value.substring(dot+1,f.length);
	
	if(fileType!="dbfile"){
		alert("dbfile 확장자만 등록할 수 있습니다.");
		f.value="";
	}
}

function chkForm(){
	var f = document.chkform;
	/* 파일 미등록시 false 리턴 */
	if(f.file.value==""){
		alert("파일을 등록해주세요");
		return false;
	}
}
</script>
</head>
<body>
	<h2>DB파일 업로드</h2>
	<form action="/response" method="post" enctype="multipart/form-data" onsubmit="return chkForm()" name="chkform">
		<input type="file" name="file" id="file" accept=".dbfile" onchange="checkFile(this)">
		<input type="submit" value="파일 업로드">
	</form>
</body>
</html>