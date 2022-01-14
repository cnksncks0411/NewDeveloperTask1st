package com.example.demo.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TaskDto;
import com.example.demo.mapper.TaskMapper;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	TaskMapper taskMapper;
	
	// 회원가입 기능
	@Override
	public int doJoin(TaskDto tDto)  {
		// 이메일 앞 부분과 뒷 부분 @로 연결
		String email = tDto.getEmail()+"@"+tDto.getEmail2();
		tDto.setEmail(email);
		int result = 0;
		try {
			result = taskMapper.doJoin(tDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 아이디 중복확인
	@Override
	public int checkId(String id) {
		int result = taskMapper.checkId(id);
		return result;
	}

	// 로그인 기능
	@Override
	public TaskDto doLogin(TaskDto taskDto) {
		TaskDto tDto = taskMapper.doLogin(taskDto);
		return tDto;
	}

	// 자동 로그인
	@Override
	public void autoLogin(String id, String sessionId, Timestamp sessionlimit) {
		taskMapper.autoLogin(id, sessionId, sessionlimit);
	}

	// 쿠키 정보 있는 지 확인
	@Override
	public TaskDto checkTime(String sessionId) {
		TaskDto tDto = taskMapper.checkTime(sessionId);
		return tDto;
	}
}
