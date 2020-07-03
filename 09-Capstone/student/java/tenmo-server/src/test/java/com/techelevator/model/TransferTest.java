package com.techelevator.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public class TransferTest {
	
	private static Transfer testTransfer;
	
	@BeforeClass
	public static void setup () {
		testTransfer = new Transfer(12, "type", "status", "from", "to", BigDecimal.TEN);
	}	

	@Test
	public void get_transfer_id_test() {
		assertEquals(12, testTransfer.getTransferId());
	}
	
	@Test
	public void get_transfer_type_test() {
		assertEquals("type", testTransfer.getTransferType());
	}
	
	@Test
	public void get_transfer_status_test() {
		assertEquals("status", testTransfer.getTransferStatus());
	}
	
	@Test
	public void get_account_from_test() {
		assertEquals("from", testTransfer.getAccountFrom());
	}
	
	@Test
	public void get_account_to_test() {
		assertEquals("to", testTransfer.getAccountTo());
	}
	
	@Test
	public void get_amount_test() {
		BigDecimal amount = BigDecimal.TEN.setScale(2);
		assertEquals(amount, testTransfer.getAmount());
	}
	
	@Test
	public void set_transfer_id_test() {
		Transfer transfer = new Transfer();
		int num = 10;
		transfer.setTransferId(num);
		assertEquals(num, transfer.getTransferId());
	}
	
	@Test
	public void set_transfer_type_test() {
		Transfer transfer = new Transfer();
		String expected = "test";
		transfer.setTransferType(expected);
		assertEquals(expected, transfer.getTransferType());
	}
	
	@Test
	public void set_transfer_status_test() {
		Transfer transfer = new Transfer();
		String expected = "test";
		transfer.setTransferStatus(expected);
		assertEquals(expected, transfer.getTransferStatus());
	}
	
	@Test
	public void set_account_from_test() {
		Transfer transfer = new Transfer();
		String expected = "test";
		transfer.setAccountFrom(expected);
		assertEquals(expected, transfer.getAccountFrom());
	}
	
	@Test
	public void set_account_to_test() {
		Transfer transfer = new Transfer();
		String expected = "test";
		transfer.setAccountTo(expected);
		assertEquals(expected, transfer.getAccountTo());
	}
	
	@Test
	public void set_amount_test() {
		Transfer transfer = new Transfer();
		BigDecimal num = BigDecimal.valueOf(9.12);
		transfer.setAmount(num);
		assertEquals(num, transfer.getAmount());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}