package com.example.demo.controller;

import java.sql.Timestamp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/user")
@SessionAttributes("login")
public class UserFunctionController{

	@Autowired
	UserService userService;
	
	// 회원가입 기능 구현
	@PostMapping("/join")
	public String join(UserDto userDto, Model model) {
		int result = userService.join(userDto);
		model.addAttribute("result", result);

		return "/user/signup";
	}
	
	// 아이디 중복확인
	@ResponseBody
	@RequestMapping(value="/checkId")
	public int idDuplicationCheck(@RequestParam("id") String id) {
		int result = userService.idDuplicationCheck(id);
		return result;
	}
	
	// 로그인 기능구현
	@PostMapping("/login")
	public String login(HttpSession session, UserDto userDto, Model model, HttpServletResponse response) {
		try {
			UserDto uDto = userService.login(userDto);
			
			if(uDto!=null) {
				model.addAttribute("login", uDto);
				// 자동 로그인 체크 했을 경우
				if(userDto.getAutoLogin()) {
					Cookie auto = new Cookie("info",session.getId());
					auto.setPath("/");
					// 쿠키 유지 시간 5분
					int amount = 60*5;
					auto.setMaxAge(amount);
					response.addCookie(auto);
					// 쿠키 만료 시간
					Timestamp sessionlimit = new Timestamp(System.currentTimeMillis()+(1000*amount));
					// 해당 유저 id에 세션key, 만료시간 저장
					userService.autoLogin(uDto.getId(), session.getId(), sessionlimit);
				}
			}else {
				int result = 1;
				model.addAttribute("result",result);
				return"/user/signin";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/main";
	}
	
	
}
