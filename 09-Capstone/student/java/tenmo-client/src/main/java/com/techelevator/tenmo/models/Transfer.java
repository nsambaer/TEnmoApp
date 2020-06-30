package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {
	
	private int transferId;
	private String transferType;
	private String transferStatus;
	private int accountFromId;
	private int accountToId;
	private BigDecimal amount;
	
	
	//Constructors
	
	public Transfer(int transferId, String transferType, String transferStatus, int accountFromId, int accountToId,
			BigDecimal amount) {

		this.transferId = transferId;
		this.transferType = transferType;
		this.transferStatus = transferStatus;
		this.accountFromId = accountFromId;
		this.accountToId = accountToId;
		this.amount = amount;
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

	public int getAccountFromId() {
		return accountFromId;
	}

	public int getAccountToId() {
		return accountToId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	
	
}
