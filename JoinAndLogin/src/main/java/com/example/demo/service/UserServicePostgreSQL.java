package com.example.demo.service;

import java.sql.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.ErrorCode;
import com.example.demo.exception.ErrorException;
import com.example.demo.exception.SignupResultFields;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserRepository;

@Service
public class UserServicePostgreSQL implements UserDataService{

	@Autowired
	UserMapper userMapper;
	@Autowired
	UtilCollection utilCollection;	// 유틸 기능 구현 모아둔 클래스
	
	// 회원가입 기능
	@Override
	public int join(UserDto userDto)  {
		// 유저 생년월일 Timestamp 포맷으로 변환하여 저장
		utilCollection.setUserBirthday(userDto);
		// Timestamp 포맷으로 변환 후 유저의 생년월일 가져오기
		Date userBirthday = utilCollection.getUserBirthday(userDto.getRegdate());
		// 비교할 현재 시간
		Date now = new Date();
		int result = 0;
		
		if(userBirthday.after(now)) {
			// BAD_REQUEST
			// 입력한 생년월일이 현재 시간보다 늦으면 에러 출력
			List<SignupResultFields> fields = utilCollection.getfields("생년월일", "생년월일은 현재 시간보다 늦을 수 없습니다.");
			throw new ErrorException(ErrorCode.NUMBER_1002,"입력항목 오류",true,fields);
		}else {
			try {
				result = userMapper.join(userDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// 회원가입 시 유효성 체크
	@Override
	public Map<String, String> validateHandling(Errors errors){
		Map<String, String> validatorResult = new HashMap<>();

		for(FieldError error : errors.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		return validatorResult;
	}

	// 아이디 중복확인
	@Override
	public int idDuplicationCheck(String id) {
		int result=0;
		try {
			result = userMapper.idDuplicationCheck(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 로그인 기능
	@Override
	public UserDto login(UserDto userDto) {
		UserDto uDto = null;
		try {
			uDto = userMapper.login(userDto);
		} catch (Exception e) {
			// DB에서 읽어올 수 없을 시, 백업된 파일에서 읽어오기
			e.printStackTrace();
		}
		return uDto;
	}

	// 자동 로그인
	@Override
	public void autoLogin(String id, String sessinKey, Timestamp sessionlimit) {
		try {
			userMapper.autoLogin(id, sessinKey, sessionlimit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 쿠키 정보 있는 지 확인
	@Override
	public UserDto checkCookieExpirationTime(String sessinKey) {
		UserDto userDto = null;
		try {
			userDto = userMapper.checkCookieExpirationTime(sessinKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userDto;
	}
	
	@Autowired
	UserRepository userRepository;
	
	// 검색 기능
	@Override
	public List<UserDto> search(String category, String keyword){
		List<UserDto> user = null;
		
		if(category.equals("id")) {
			user = userRepository.findByIdContaining(keyword);
		}else if(category.equals("name")) {
			user = userRepository.findByNameContaining(keyword);
		}else if(category.equals("level")) {
			user = userRepository.findByLevel(keyword.toUpperCase());
		}else if(category.equals("desc")) {
			user = userRepository.findByDescContaining(keyword);
		}
		return user;
	}

	@Override
	public String getServiceId() {
		return "postgreSqlService";
	}
}