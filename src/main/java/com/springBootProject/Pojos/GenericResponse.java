package com.springBootProject.Pojos;

public class GenericResponse {

	private String message;
	private Object data;

	public GenericResponse(String message) {
		this.message = message;
	}

	public GenericResponse(String message, Object data) {
		this.message = message;
		this.data = data;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
