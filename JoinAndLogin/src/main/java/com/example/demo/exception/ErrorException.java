package com.example.demo.exception;

import java.util.List;

public class ErrorException extends RuntimeException{
	private static final long serialVersionUID = 7222755606311598747L;
	
	private final ErrorCode errorCode;
	private String message = null;
	private boolean hasFieldsError = true;
	private List<SignupResultFields> fields = null;
	
	public ErrorException(ErrorCode errorCode, String message, boolean hasFieldsError, List<SignupResultFields> fields){
		this.errorCode = errorCode;
		this.message = message;
		this.hasFieldsError = hasFieldsError;
		this.fields = fields;
	}
	
	public ErrorCode getErrorCode() {
		return this.errorCode;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public boolean isHasFieldsError() {
		return this.hasFieldsError;
	}
	
	public List<SignupResultFields> getFields(){
		return this.fields;
	}
}
