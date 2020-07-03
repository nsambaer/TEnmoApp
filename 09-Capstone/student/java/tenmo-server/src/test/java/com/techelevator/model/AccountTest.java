package com.techelevator.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;

import com.techelevator.tenmo.model.Account;

public class AccountTest {

	private static Account testAccount;
	
	@BeforeClass
	public static void setup () {
		testAccount = new Account(9, 12, BigDecimal.valueOf(300));
	}	

	@Test
	public void get_account_id_test() {
		assertEquals(9, testAccount.getAccountId());
	}
	
	@Test
	public void get_user_id_test() {
		assertEquals(12, testAccount.getUserId());
	}
	
	@Test
	public void get_balance_test() {
		BigDecimal balance = BigDecimal.valueOf(300.00).setScale(2);
		assertEquals(balance, testAccount.getBalance());
	}
	
	@Test
	public void set_account_id_test() {
		Account account = new Account();
		int num = 10;
		account.setAccountId(num);
		assertEquals(num, account.getAccountId());
	}
	
	@Test
	public void set_user_id_test() {
		Account account = new Account();
		int num = 10;
		account.setUserId(num);
		assertEquals(num, account.getUserId());
	}
	
	@Test
	public void set_Balance_test() {
		Account account = new Account();
		BigDecimal num = BigDecimal.valueOf(9.12);
		account.setBalance(num);
		assertEquals(num, account.getBalance());
	}
	
}
