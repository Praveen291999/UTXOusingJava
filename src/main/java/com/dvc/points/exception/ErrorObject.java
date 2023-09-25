package com.dvc.points.exception;

public class ErrorObject {

	private int statusCode;
	private String message;
	private long timestam;

	public ErrorObject(int statusCode, String message, long timestam) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.timestam = timestam;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimestam() {
		return timestam;
	}

	public void setTimestam(long timestam) {
		this.timestam = timestam;
	}

}