package com.wing.ydt;

public class WingException extends RuntimeException {
	private static final long serialVersionUID = -3172227172337817271L;

	public WingException(String message) {
		super(message);
	}
	
	public WingException(Throwable t) {
		super(t);
	}
	
	public WingException(String message, Throwable t) {
		super(message, t);
	}
}
