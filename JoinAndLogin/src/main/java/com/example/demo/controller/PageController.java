package com.example.demo.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;

@Controller
@RequestMapping(value="/user")
public class PageController {

	@Autowired
	UserService userService;
	// 메인 페이지
	@RequestMapping(value="/main")
	public String main() {
		return "/user/main";
	}

	// 회원가입 페이지
	@GetMapping("/signup")
	public String join() {
		return "/user/signup";
	}
	
	// 로그인 페이지
		@RequestMapping("/signin")
		public String loginPage(HttpServletRequest request, HttpSession session){
			Object obj = session.getAttribute("login");
			// session이 비어있다면
			if(obj==null) {
				// 쿠키를 가져옴
				Cookie cookie = WebUtils.getCookie(request, "info");
				// 쿠키가 존재한다면
				if(cookie!=null) {
					String sessinKey = cookie.getValue();
					// 쿠키 만료시간과 현재 시간 비교 후 만료되지 않은 정보 저장
					UserDto userDto = userService.checkCookieExpirationTime(sessinKey);
					// 쿠키가 만료되지 않았다면
					if(userDto!=null) {
						session.setAttribute("login", userDto);
						return "/user/main";
					}
				}
				return "/user/signin";
			}
			return "/user/main";
		}

	// 로그아웃 페이지
	@RequestMapping(value="/signout")
	public String logout() {
		return "/user/signout";
	}
}
