package com.techelevator.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;

import com.techelevator.tenmo.models.Transfer;

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
	public void list_overview_test() {
		String username = "from";
		String overview = "12        To: to     $10.00";
		assertEquals(overview, testTransfer.listOverview(username));
	}
	
	@Test
	public void to_string_test() {
		String output =  " ID: 12\n From: from\n To: to\n Type: type\n Status: status\n Amount: $10.00";
		assertEquals(output, testTransfer.toString());
	}
}
