package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

//@PreAuthorize("isAuthenticated()")
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
	

	@RequestMapping(path = "/users/{id}/account", method = RequestMethod.GET)
	public Account listUserAccountInfo(@PathVariable int id) {
		return aDAO.getAccountByUserId(id);
	}
	
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public List<User> listUsers() {
		return uDAO.findAll();
	}
	
}
