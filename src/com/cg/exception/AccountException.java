package com.cg.exception;

public class AccountException extends Exception {
	String message;
	public AccountException(String message) {
		super(message);
		this.message = message;
	}
}
