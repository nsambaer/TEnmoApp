package com.techelevator.tenmo.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class Transfer {

	private int transferId;
	@NotBlank(message = "Transfer type should not be blank")
	private String transferType;
	@NotBlank(message = "Transfer status should not be blank")
	private String transferStatus;
	@NotBlank(message = "Account being transferred from should not be blank")
	private String accountFrom;
	@NotBlank(message = "Account being transferred to should not be blank")
	private String accountTo;
	@DecimalMin(value = "0.01", message = "Transfer amount should be more than zero")
	private BigDecimal amount;

	public Transfer() {

	}

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

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public String getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}

	public String getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(String accountTo) {
		this.accountTo = accountTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
		this.amount = this.amount.setScale(2);
	}

}
