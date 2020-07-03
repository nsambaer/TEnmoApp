package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {
	
	public Account getAccountByUserId(long userId);

	public Account updateAccountBalance(String username, BigDecimal amount) throws OverdraftException;
	
	//public Account getAccountByAccountId(int accountId);

}
