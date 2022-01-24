package com.example.demo.dto;

public class UserDto {
	// getter, setter, constructor 직접 생성
	private String id = null;
	private String pw = null;
	private String name = null;
	private String level = null;
	private String desc = null;
	private String regDate = null;
	private boolean autoLogin = true;
	
	public UserDto(){
		
	}
	
	public UserDto(String id, String pw, String name, String level, String desc, String regDate, boolean autoLogin){
		this.id=id;
		this.pw=pw;
		this.name=name;
		this.level=level;
		this.desc=desc;
		this.regDate=regDate;
		this.autoLogin=autoLogin;
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
	
	public String getRegDate() {
		return regDate;
	}
	
	public boolean getAutoLogin() {
		return autoLogin;
	}
	
	public void setId(String id) {
		this.id=id;
	}
	
	public void setPw(String pw) {
		this.pw=pw;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void setLevel(String level) {
		this.level=level;
	}
	
	public void setDesc(String desc) {
		this.desc=desc;
	}
	
	public void setRegDate(String regDate) {
		this.regDate=regDate;
	}
	
	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin=autoLogin;
	}
}
