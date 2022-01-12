package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dto.TaskDto;
import com.example.demo.service.TaskService;

@Controller
public class TaskController {

	@Autowired
	TaskService taskService;
	
	// 파일 업로드 페이지
	@RequestMapping("/upload")
	public String upLoad(Model model) {
		return "/upload";
	}
	
	// 파일 업로드 결과 페이지
	@RequestMapping("/response")
	public String doUpload(MultipartFile file, Model model) {
		HashMap<String, Object> map = taskService.read(file);
		model.addAttribute("map", map);
		return "/response";
	}
	
	// 레코드 조회 시 db테이블 조회 결과를 json포맷으로 응답
	@ResponseBody
	@RequestMapping("/selectData")
	public JSONArray selectData(Model model) {
		List<TaskDto> list = taskService.selectData();
		JSONArray arr = new JSONArray();
		
		for(TaskDto td:list) {
			JSONObject obj = new JSONObject();
			obj.put("id",td.getId());
			obj.put("pwd",td.getPwd());
			obj.put("name",td.getName());
			obj.put("level",td.getLevel());
			obj.put("desc",td.getDesc());
			obj.put("reg_date",td.getReg_date());
			arr.add(obj);
		}

		return arr;
	}
}
