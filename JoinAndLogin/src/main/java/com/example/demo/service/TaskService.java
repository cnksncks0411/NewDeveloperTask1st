package com.example.demo.service;

import com.example.demo.dto.TaskDto;

public interface TaskService {

	// 회원가입 기능
	int doJoin(TaskDto tDto);
	
	// 아이디 중복확인
	int checkId(String id);
	
	// 로그인 기능
	TaskDto doLogin(TaskDto taskDto);
}
