package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dto.TaskDto;

public interface TaskService {
	// 파일 내용 읽어 HashMap에 담기
	HashMap<String, Object> read(MultipartFile file);
	// DB테이블 내용 List에 담기
	List<TaskDto> selectData();
}
