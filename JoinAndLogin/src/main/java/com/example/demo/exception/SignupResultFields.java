package com.example.demo.exception;

public class SignupResultFields {
	private String field;
	private String reason;
	
	public SignupResultFields(String field, String reason) {
		this.field=field;
		this.reason=reason;
	}
	
	public String getField() {
		return this.field;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public void setField(String field) {
		this.field=field;
	}
	
	public void setReason(String reason) {
		this.reason=reason;
	}
}
