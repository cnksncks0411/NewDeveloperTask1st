package com.example.demo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.example.demo.dto.TaskDto;

@Mapper
public interface TaskMapper {
	// 파일 내용 DB에 저장
	void insertFile(TaskDto tDto);
	// DB 테이블 조회
	List<TaskDto> selectData();
}
