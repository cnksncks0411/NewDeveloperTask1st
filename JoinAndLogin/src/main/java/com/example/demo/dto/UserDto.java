package com.example.demo.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Builder;

@Entity
@Table(name="userinfo", schema="public")
public class UserDto {
	// getter, setter, constructor 직접 생성

	@Id
	@NotBlank(message = "아이디는 필수 입력 값입니다.")
	@Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9]{3,15}$", message = "아이디는 영문자로 시작하여 4~16자의 영문, 숫자 조합이어야 합니다.")
	private String id = null;

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]{8,16}$", message = "비밀번호는 8~16자의 영문, 숫자 조합이어야 합니다.")
	private String pw = null;

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	@Pattern(regexp = "^[가-힣]{2,8}$", message = "이름은 한글로만 입력 할 수 있습니다.")
	private String name = null;

	@NotBlank(message = "회원등급은 필수 입력 값입니다.")
	private String level = null;
	private String desc = null;

	@NotBlank(message = "생년월일은 필수 입력 값입니다.")
	private String regdate = null;

	@Transient
	private boolean autoLogin = false;

	public UserDto() {

	}

	@Builder
	public UserDto(String id, String pw, String name, String level, String desc, String regdate, boolean autoLogin) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.level = level;
		this.desc = desc;
		this.regdate = regdate;
		this.autoLogin = autoLogin;
	}

	public String getId() {
		return id;
	}

	public String getPw() {
		return pw;
	}

	public String getName() {
		return name;
	}

	public String getLevel() {
		return level;
	}

	public String getDesc() {
		return desc;
	}

	public String getRegdate() {
		return regdate;
	}

	public boolean getAutoLogin() {
		return autoLogin;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}
}
