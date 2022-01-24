package com.example.demo.service;

import java.sql.Timestamp;

import com.example.demo.dto.UserDto;

public interface UserDataService {
	
	// 회원가입 기능
	int join(UserDto userDto);
	
	// 아이디 중복확인
	int idDuplicationCheck(String id);
	
	// 로그인 기능
	UserDto login(UserDto userDto);
	
	// 자동 로그인
	void autoLogin(String id, String sessinKey, Timestamp sessionlimit);
	
	// 쿠키 정보 있는 지 확인
	UserDto checkCookieExpirationTime(String sessinKey);
	
	
}
