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
			output += "To: " + accountTo;
		} else {
			output += "From: " + accountFrom;
		}
		output += "     ";
		output += "$" + amount;
		return output;
	}
	
	@Override
	public String toString() {
		return " ID: " + transferId + "\n From: " + accountFrom + "\n To: " + accountTo + "\n Type: " + transferType + "\n Status: " + transferStatus
				+ "\n Amount: $" + amount;
	}
	
	
	
}
