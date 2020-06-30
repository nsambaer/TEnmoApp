package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Account {
	
	private Long accountId;
	private Long userId;
	private BigDecimal balance;
	
	
	//Constructors
	
	public Account() {
		
	}
	public Account(Long accountId, Long userId, BigDecimal balance) {
		this.accountId = accountId;
		this.userId = userId;
		this.balance = balance;
	}
	
	
	
	public Long getAccountId() {
		return accountId;
	}
	public Long getUserId() {
		return userId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
}
