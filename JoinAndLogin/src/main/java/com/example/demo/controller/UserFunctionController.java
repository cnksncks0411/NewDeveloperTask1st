package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserDataService;

@Controller
@RequestMapping("/user")
@SessionAttributes("login")
public class UserFunctionController{

	@Autowired
	// @Qualifier("userServicePostgreSQL") => DB 연결
	// @Qualifier("userServiceNoneDB") => 백업 파일 연결
	@Qualifier("userServicePostgreSQL")
	UserDataService userDataService;
	
	// 회원가입 기능 구현
	@PostMapping("/signup")
	public String join(@Valid UserDto userDto, Errors errors, Model model) {
		int result = 0;
		if(errors.hasErrors()) {
			// 회원가입 실패 시, 입력 데이터를 유지
			model.addAttribute("userDto",userDto);
			
			// 유효성 통과 못한 필드와 메시지 핸들링
			Map<String, String> validatorResult = userDataService.validateHandling(errors);
			for(String key : validatorResult.keySet()) {
				model.addAttribute(key, validatorResult.get(key));
			}
			return "/user/signup";
		}
		result = userDataService.join(userDto);
		model.addAttribute("result", result);
		return "/user/signup";

	}
	
	// 아이디 중복확인
	@ResponseBody
	@RequestMapping(value="/checkId")
	public int idDuplicationCheck(@RequestParam("id") String id) {
		int result = userDataService.idDuplicationCheck(id);
		return result;
	}
	
	// 로그인 기능구현
	@PostMapping("/login")
	public String login(HttpSession session, UserDto userDto, Model model, HttpServletResponse response) {
		try {
			UserDto uDto = userDataService.login(userDto);
			if(uDto!=null) {
				model.addAttribute("login", uDto);
				// 자동 로그인 체크 했을 경우
				if(userDto.getAutoLogin()) {
					Cookie auto = new Cookie("info",session.getId());
					auto.setPath("/");
					// 쿠키 유지 시간 5분
					int amount = 60;
					auto.setMaxAge(amount);
					response.addCookie(auto);
					// 쿠키 만료 시간
					Timestamp sessionlimit = new Timestamp(System.currentTimeMillis()+(1000*amount));
					// 해당 유저 id에 세션key, 만료시간 저장
					userDataService.autoLogin(uDto.getId(), session.getId(), sessionlimit);
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