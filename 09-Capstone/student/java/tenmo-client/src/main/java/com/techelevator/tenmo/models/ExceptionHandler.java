package com.techelevator.tenmo.models;

public class ExceptionHandler {
	/*
		The goal of this class is to allow for easy printing of different parts of the exception message
	*/
	private String timestamp;
	private String status;
	private String error;
	private String message;
	private String path;
	
	//the constructor will take in the message and split it into the different components
	public ExceptionHandler(String rawMessage) {
		String[] raw = rawMessage.split(",");
		
		
		try {
		this.timestamp = raw[0];
		this.status = raw[1];
		this.error = raw[2];
		this.message = raw[3];
		this.path = raw[4];
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(rawMessage);
		}
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getStatus() {
		this.status = this.status.substring(9);
		return status;
	}

	public String getError() {
		this.error = this.error.substring(9, this.error.length()-1);
		return this.error;
	}

	public String getMessage() {
		this.message = this.message.substring(11, this.message.length()-1);
		return message;
	}

	public String getPath() {
		return path;
	}
	
	@Override
	public String toString() {
		return getStatus() + " " + getError() + "\n" + getMessage();
	}
	
	
}
