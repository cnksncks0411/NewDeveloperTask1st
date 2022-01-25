package com.example.demo.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.ErrorCode;
import com.example.demo.exception.ErrorException;
import com.example.demo.exception.SignupResultFields;

@Component
public class UtilCollection {

	// 파일 읽기
	public File createFile() {
		File backupFile = new File("C:/backup/dataBackup.csv");
		return backupFile;
	}
	
	// 유저 생년월일 얻기
	public Date getUserBirthday(String regDate) {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date userBirthday = null;
		try {
			userBirthday = transFormat.parse(regDate);
		} catch (ParseException e2) {
			e2.printStackTrace();
			// 기타 오류
			SignupResultFields s = new SignupResultFields("","알 수 없음");
			List<SignupResultFields> fields = new ArrayList<SignupResultFields>();
			fields.add(s);
			throw new ErrorException(ErrorCode.NUMBER_1003,"기타 오류",true,fields);
		}
		return userBirthday;
	}
	
	// 유저 생년월일 Timestamp 변환
	public void setUserBirthday(UserDto userDto) {
		String regDate = userDto.getRegDate().replace('T', ' ');
		regDate +=":00";
		userDto.setRegDate(regDate);
	}
	
	// 에러 필드
	public List<SignupResultFields> getfields(String field, String reason){
		SignupResultFields s = new SignupResultFields(field, reason);
		List<SignupResultFields> fields = new ArrayList<SignupResultFields>();
		fields.add(s);
		
		return fields;
	}
}
