package com.example.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dto.TaskDto;
import com.example.demo.mapper.TaskMapper;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	TaskMapper taskMapper;
	
	// 파일 내용 읽어 HashMap에 담기
	@Override
	public HashMap<String, Object> read(MultipartFile file) {
		String fileSaveUrl = "C:/fileSave/";
		long time = System.currentTimeMillis();
		String newFileName = String.format("%d_%s", time, file.getOriginalFilename());	// 파일명 중복되지 않게 저장
		File f = new File(fileSaveUrl+newFileName);
		int success=0;
		int count=0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<ArrayList<Object>> failList = new ArrayList<ArrayList<Object>>();
		
		try {
			file.transferTo(f);	// 파일 저장
			FileReader reader = new FileReader(f);	// 파일 읽어오기
			BufferedReader bufReader = new BufferedReader(reader);	// 파일 내용 라인별로 읽어오기 위해 사용
			String line = "";
			
			// 파일 내용 한 줄씩 읽기
			while((line=bufReader.readLine())!=null){
				String[] list = line.split("/");
				// list[0]=id, list[1]=pwd, list[2]=name, list[3]=level, list[4]=desc, list[5]=reg_date
				TaskDto tDto = new TaskDto(list[0],list[1],list[2],list[3].charAt(0),list[4],Timestamp.valueOf(list[5]));
				count++;
				try {
					taskMapper.insertFile(tDto);	// 파일 내용 DB에 저장
					success++;
				}catch (Exception e) {
					ArrayList<Object> arr = new ArrayList<Object>();
					arr.add(count);
					arr.add(line);
					// 저장에 실패한 파일 내용 리스트에 담기
					failList.add(arr);
					// 저장에 실패하더라도 중단하지 않고 다음 라인 처리
					continue;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			System.out.println(e);
		}
		
		map.put("count", count);
		map.put("success",success);
		map.put("failList", failList);
		
		return map;
	}

	// DB테이블 내용 List에 담기
	@Override
	public List<TaskDto> selectData() {
		List<TaskDto> list = taskMapper.selectData();
		return list;
	}

}
