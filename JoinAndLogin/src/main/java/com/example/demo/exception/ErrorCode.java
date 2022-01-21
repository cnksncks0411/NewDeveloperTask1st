package com.example.demo.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape=Shape.OBJECT)
public enum ErrorCode {

	NUMBER_1001(1001)
	,NUMBER_1002(1002)
	,NUMBER_1003(1003)
	,NUMBER_1004(1004)
	;
	
	private int code;
	
	ErrorCode(int code){
		this.code=code;
	}
	
	public int getCode() {
		return code;
	}
}
