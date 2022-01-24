package com.example.demo.exception;

import java.util.List;

import org.springframework.http.ResponseEntity;

import lombok.Builder;

@Builder
public class ErrorResponse{
	
	private int code=0;
	private String message = null;
	private boolean hasFieldsError = true;
	private List<SignupResultFields> fields = null;
	
	public ErrorResponse() {
		
	}
	
	public ErrorResponse(int code, String message, boolean hasFieldsError, List<SignupResultFields> fields) {
		this.code=code;
		this.message=message;
		this.hasFieldsError=hasFieldsError;
		this.fields=fields;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public boolean getHasFieldsError() {
		return this.hasFieldsError;
	}
	
	public List<SignupResultFields> getFields(){
		return this.fields;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setHasFieldsError(boolean hasFieldsError) {
		this.hasFieldsError = hasFieldsError;
	}
	
	public void setFields(List<SignupResultFields> fields) {
		this.fields = fields;
	}
	
	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, 
				String message, boolean hasFieldsError, List<SignupResultFields> list){
		return ResponseEntity
				.status(400)
				.body(ErrorResponse.builder()
						.code(errorCode.getCode())
						.message(message)
						.hasFieldsError(hasFieldsError)
						.fields(list)
						.build()
				);
	}
}
