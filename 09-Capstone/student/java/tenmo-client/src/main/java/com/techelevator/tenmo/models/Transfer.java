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

	//this returns a small overview to be used when displaying transaction history to a user
	public String listOverview(String username) {
		String output = "";
		
		String formatTransferId = String.valueOf(transferId);
		int transferIdLength = formatTransferId.length();
		for(int i = 0; i <= (8 - transferIdLength); i++) {
			formatTransferId += " ";
		}
		output += formatTransferId;
		
		if (username.equals(accountFrom)) {
			String formatAccountTo = accountTo;
			int accountToLength = formatAccountTo.length();
			output += "To: ";
			for (int i = 0; i <= (16 - accountToLength); i++) {
				formatAccountTo += " ";
			}
			output += formatAccountTo;
		}
		else {
			String formatAccountFrom = accountFrom;
			int accountFromLength = formatAccountFrom.length();
			output += "From: ";
			for (int i = 0; i <= (14 - accountFromLength); i++) {
				formatAccountFrom += " ";
			}
			output += formatAccountFrom;
		}
		
		String formatAmount = "$" + String.valueOf(amount);
		int amountLength = formatAmount.length();
		for(int i = 0; i <= (10 - amountLength); i++) {
			formatAmount += " ";
		}
		output += formatAmount;
		
		return output;
		
//		String output = transferId + "        ";
//		if (username.equals(accountFrom)) { //checking to see if the user sent or received the transaction in order to show them the relevant to or from field
//			output += "To: " + accountTo;
//		} 
//		else {
//			output += "From: " + accountFrom;
//		}
//		output += "     ";
//		output += "$" + amount;
//		return output;
	}
	
	@Override
	public String toString() { //this is used to show the full transaction details
		return " ID: " + transferId + "\n From: " + accountFrom + "\n To: " + accountTo + "\n Type: " + transferType + "\n Status: " + transferStatus
				+ "\n Amount: $" + amount;
	}
	
	
	
}
