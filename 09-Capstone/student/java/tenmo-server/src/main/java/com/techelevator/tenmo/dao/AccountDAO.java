package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {
	
	public Account getAccountByUserId(int userId);

	public Account updateAccountBalance(int userId, BigDecimal amount);
	
	//public Account getAccountByAccountId(int accountId);

}
