package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.view.ConsoleService;

public class TransferService {

	//this class was created to manage all transfer-related methods
	
	private TenmoService tenmoService;
	private ConsoleService console;

	public TransferService(ConsoleService console, TenmoService tenmoService) {
		this.tenmoService = tenmoService;
		this.console = console;
	}

	public Transfer sendTransfer(User fromUser) throws TenmoServiceException {
		String fromAccountUsername = fromUser.getUsername();
		BigDecimal amount;
		String prompt = "\nEnter ID of user you are sending to (0 to cancel)";
		String toAccountUsername = selectUser(fromUser.getId(), prompt);
		if (toAccountUsername == null) { // if toAccountUsername is null, the user entered a zero and wants to cancel the transfer
			return null;
		}
		while (true) {
			amount = getAmount();
			Account account = tenmoService.listCurrentBalance(fromUser.getId());
			if (amount.compareTo(account.getBalance()) > 0) {
				System.out.println("Your requested transfer amount will overdraft your account. Please try again.");
			}
			else {
				break;
			}
		}
		String status = "Approved";
		String type = "Send";
		Transfer transfer = new Transfer(0, type, status, fromAccountUsername, toAccountUsername, amount);
		return transfer;
	}
	
	public Transfer requestTransfer(User toUser) throws TenmoServiceException {
		String toAccountUsername = toUser.getUsername();
		BigDecimal amount;
		String prompt = "\nEnter ID of user you are requesting from (0 to cancel)";
		String fromAccountUsername = selectUser(toUser.getId(), prompt);
		if (fromAccountUsername == null) { // if toAccountUsername is null, the user entered a zero and wants to cancel the transfer
			return null;
		}
		amount = getAmount();
		
		String status = "Pending";
		String type = "Request";
		Transfer transfer = new Transfer(0, type, status, fromAccountUsername, toAccountUsername, amount);
		return transfer;
	}

	public BigDecimal getAmount() {
		while (true) {
			String amountString = console.getUserInput("Enter amount");
			try {
				double amountDouble = Double.parseDouble(amountString);
				return BigDecimal.valueOf(amountDouble);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
			}
		}
	}

	public String selectUser(int currentUserId, String prompt) throws TenmoServiceException {
		List<User> userList = new ArrayList<>();
		userList = tenmoService.viewAllUsers();
		
		//this block is to prevent the user's own id from showing up in the list of people to send or request a transfer from
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getId() == currentUserId) {
				userList.remove(i);
			}
		}

		System.out.println("------------------------------------------");
		System.out.println("Users");
		System.out.println("ID       Name");
		System.out.println("------------------------------------------");
		for (User u : userList) {
			System.out.println(u);
		}
		while (true) {
			int userId = console.getUserInputInteger(prompt);
			//validation for invalid user ID entries such as negative value or value that does not exist
			if (userId < 0) {
				System.out.println("You have entered a negative number. Please try again.");
			} else {
				if (userId == 0) {
					return null;
				}
				for (User u : userList) {
					if (userId == u.getId()) {
						return u.getUsername();
					}
				}
				System.out.println("You have entered an invalid user ID. Please try again.");

			}
		}
	}

}
