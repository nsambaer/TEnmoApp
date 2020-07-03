package com.techelevator.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;

import com.techelevator.tenmo.models.Account;

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
	public void to_string_test() {
		String balance = "$300.00";
		assertEquals(balance, testAccount.toString());
	}
	
	
	
	
	
}
