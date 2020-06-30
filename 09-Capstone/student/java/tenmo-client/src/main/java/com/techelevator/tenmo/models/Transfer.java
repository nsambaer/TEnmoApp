package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {
	
	private long transferId;
	private String transferType;
	private String transferStatus;
	private long accountFromId;
	private long accountToId;
	private BigDecimal amount;
	
	
	//Constructors
	
	public Transfer(long transferId, String transferType, String transferStatus, long accountFromId, long accountToId,
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
	
	

	public long getTransferId() {
		return transferId;
	}

	public String getTransferType() {
		return transferType;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public long getAccountFromId() {
		return accountFromId;
	}

	public long getAccountToId() {
		return accountToId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	
	
}
