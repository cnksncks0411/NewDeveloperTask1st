package com.example.demo.controller;

import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

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
	public String login(HttpServletRequest request){
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("login");
		// session이 비어있다면
		if(obj==null) {
			// 쿠키를 가져옴
			Cookie cookie = WebUtils.getCookie(request, "info");
			// 쿠키가 존재한다면
			if(cookie!=null) {
				String sessionId = cookie.getValue();
				// 쿠키 만료시간과 현재 시간 비교 후 만료되지 않은 정보 저장
				TaskDto tDto = taskService.checkTime(sessionId);
				// 쿠키가 만료되지 않았다면
				if(tDto!=null) {
					session.setAttribute("login", tDto);
					return "/index";
				}
				
			}
			return "/login";
		}
		return "/index";
	}
	
	// 로그인 기능구현
	@RequestMapping("/doLogin")
	public String doLogin(HttpServletRequest request, TaskDto taskDto, Model model, HttpServletResponse response) {
		HttpSession session = request.getSession();
		TaskDto tDto = taskService.doLogin(taskDto);
		
		int result = 0;
		
		if(tDto!=null) {
			result = 1;
			session.setAttribute("login", tDto);
			// 자동 로그인 체크 했을 경우
			if(taskDto.isAutoLogin()) {
				Cookie auto = new Cookie("info",session.getId());
				auto.setPath("/");
				// 쿠키 유지 시간 2분
				int amount = 60*5;
				auto.setMaxAge(amount);
				response.addCookie(auto);
				// 쿠키 만료 시간
				Timestamp sessionlimit = new Timestamp(System.currentTimeMillis()+(1000*amount));
				// 해당 유저 id에 세션key, 만료시간 저장
				taskService.autoLogin(tDto.getId(), session.getId(), sessionlimit);
			}
			
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
