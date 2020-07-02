package com.techelevator.tenmo.dao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot transfer more money than you have in your account")
public class OverdraftException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OverdraftException() {
		super("Cannot transfer more money than you have in your account");
	}
}
