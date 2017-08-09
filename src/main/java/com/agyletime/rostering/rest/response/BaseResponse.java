package com.agyletime.rostering.rest.response;

public class BaseResponse {

	private int code;
	private String status;
	private String message;
	
	public BaseResponse() {
		this(200, "SUCCESS", "SUCCESS");
	}
	public BaseResponse(int code, String status) {
		super();
		this.code = code;
		this.status = status;
		this.message = status;
	}
	public BaseResponse(int code, String status, String message) {
		super();
		this.code = code;
		this.status = status;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public String getStatus() {
		return status;
	}
	public String getMessage() {
		return message;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
