package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.TaskDto;
import com.example.demo.service.TaskService;

@Controller
public class TaskController {

	@Autowired
	TaskService taskService;
	
	// 메인 페이지
	@RequestMapping("/index")
	public String index() {
		return "/index";
	}
	
	// 회원가입 페이지
	@RequestMapping("/join")
	public String join() {
		return "/join";
	}
	
	// 회원가입 기능 구현
	@RequestMapping("/doJoin")
	public String doing(TaskDto tDto, Model model) {
		int result = taskService.doJoin(tDto);
		model.addAttribute("result", result);
		return "/join";
	}
	
	// 아이디 중복확인
	@ResponseBody
	@RequestMapping("/checkId")
	public int check(@RequestParam("id") String id) {
		int result = taskService.checkId(id);
		return result;
	}
	
	// 로그인 페이지
	@RequestMapping("/login")
	public String login() {
		return "/login";
	}
	
	// 로그인 기능구현
	@RequestMapping("/doLogin")
	public String doLogin(HttpServletRequest request, TaskDto taskDto, Model model) {
		HttpSession session = request.getSession();
		TaskDto tDto = taskService.doLogin(taskDto);
		int result = 0;
		
		if(tDto!=null) {
			result = 1;
			session.setAttribute("session_id", tDto.getId());
			session.setAttribute("session_name", tDto.getName());
		}
		model.addAttribute("result", result);
		return "/login";
	}
	
	// 로그아웃
	@RequestMapping("/logout")
	public String logout() {
		return "/logout";
	}
}
