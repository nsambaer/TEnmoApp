package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {
	
	public Account getAccountByUserId(int userId);
	
	//public Account getAccountByAccountId(int accountId);
	
	

}
