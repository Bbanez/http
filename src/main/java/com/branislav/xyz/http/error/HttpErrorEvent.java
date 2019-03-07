package com.branislav.xyz.http.error;

public class HttpErrorEvent {

	private int status;
	private String message;
	
	public HttpErrorEvent()	{
		status = -1;
		message = null;
	}
	
	public HttpErrorEvent(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "{status:" + status + ", message:" + message + "}";
	}
}
