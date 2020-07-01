package com.techelevator.tenmo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@PreAuthorize("isAuthenticated()")
@RestController
public class TenmoController {

	private AccountDAO aDAO;
	private TransferDAO tDAO;
	private UserDAO uDAO;
	
	public TenmoController(AccountDAO accountDAO, TransferDAO transferDAO, UserDAO userDAO) {
		this.aDAO = accountDAO;
		this.tDAO = transferDAO;
		this.uDAO = userDAO;
	}
	
	
	@RequestMapping(path = "/account/{userId}", method = RequestMethod.GET)
	public Account listUserAccountInfo(@PathVariable int userId) {
		return aDAO.getAccountByUserId(userId);
	}
	
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public List<User> listUsers() {
		return uDAO.findAllUsersSanitized();
	}
	
	@RequestMapping(path = "/account/{userId}/transfers", method = RequestMethod.POST)
	public Transfer makeTransfer(@Valid @RequestBody Transfer transfer) {		
		
		aDAO.updateAccountBalance(transfer.getAccountFromId(), transfer.getAmount().negate());
		aDAO.updateAccountBalance(transfer.getAccountToId(), transfer.getAmount());
		return tDAO.createTransfer(transfer);
	}
	
	@RequestMapping(path = "account/{userId}/transfers", method = RequestMethod.GET)
	public List<Transfer> listTransferHistory(@PathVariable int userId) {
		return tDAO.getTransferHistoryByUserId(userId);
	}
	
	
	
	
	
}
