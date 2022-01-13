package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.TaskDto;

@Mapper
public interface TaskMapper {

	// 회원가입 기능
	int doJoin(TaskDto tDto);

	// 아이디 중복확인
	int checkId(String id);
	
	// 로그인 기능
	TaskDto doLogin(TaskDto taskDto);
}
