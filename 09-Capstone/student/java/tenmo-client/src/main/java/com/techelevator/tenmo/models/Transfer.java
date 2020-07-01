package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {
	
	private int transferId;
	private String transferType;
	private String transferStatus;
	private String accountFrom;
	private String accountTo;
	private BigDecimal amount;
	
	
	//Constructors
	
	public Transfer(int transferId, String transferType, String transferStatus, String accountFrom, String accountTo,
			BigDecimal amount) {

		this.transferId = transferId;
		this.transferType = transferType;
		this.transferStatus = transferStatus;
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;
		this.amount = this.amount.setScale(2);
	}

	public Transfer() {
	}

	public int getTransferId() {
		return transferId;
	}

	public String getTransferType() {
		return transferType;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public String getAccountFrom() {
		return accountFrom;
	}

	public String getAccountTo() {
		return accountTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String listOverview(String username) {
		String output = transferId + "        ";
		if (username.equals(accountFrom)) {
			output += "From: " + accountFrom;
		} else {
			output += "To: " + accountTo;
		}
		output += "     ";
		output += "$" + amount;
		return output;
	}
	
	
	
	
}
