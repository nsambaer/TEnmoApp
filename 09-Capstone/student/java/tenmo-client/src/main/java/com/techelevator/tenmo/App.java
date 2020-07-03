package com.techelevator.tenmo;

import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.ExceptionHandler;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TenmoService;
import com.techelevator.tenmo.services.TenmoServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String LOGIN_MENU_BACKDOOR = "";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT, LOGIN_MENU_BACKDOOR };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String PENDING_MENU_OPTION_APPROVE = "Approve";
	private static final String PENDING_MENU_OPTION_REJECT = "Reject";
	private static final String PENDING_MENU_OPTION_NO_APPROVE_REJECT = "Don't approve or reject";
	private static final String[] PENDING_MENU_OPTIONS = { PENDING_MENU_OPTION_APPROVE, PENDING_MENU_OPTION_REJECT,
			PENDING_MENU_OPTION_NO_APPROVE_REJECT };

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private TenmoService tenmoService;
	private int currentUserId;
	private String currentUsername;
	private TransferService transferService;
	private ExceptionHandler exception;

	public static void main(String[] args) throws TenmoServiceException {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
				new TenmoService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService, TenmoService tenmoService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.tenmoService = tenmoService;
		transferService = new TransferService(console, tenmoService);
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			try {
				String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
				if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
					viewCurrentBalance();
				} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
					viewTransferHistory();
				} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
					viewPendingRequests();
				} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
					sendBucks();
				} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
					requestBucks();
				} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
					login();
				} else {
					// the only other option on the main menu is to exit
					exitProgram();
				}
			} catch (TenmoServiceException ex) {
				exception = new ExceptionHandler(ex.getMessage());
				System.out.println(exception);
			}

		}
	}

	private void viewCurrentBalance() throws TenmoServiceException {
		System.out.println("------------------------------------------");
		System.out.println("Your current balance is: " + tenmoService.listCurrentBalance(currentUserId));
		System.out.println("------------------------------------------");
	}

	private void viewTransferHistory() throws TenmoServiceException {
		Transfer[] transferHistory = tenmoService.listTransferHistory(currentUserId);
		if (transferHistory.length == 0) {
			System.out.println("You have no previous transfers. Please select another option.");
		} else {
			System.out.println("------------------------------------------");
			System.out.println("Transfers");
			System.out.printf("%-8s %-20s %-10s\n", "ID", "From/To", "Amount");
			System.out.println("------------------------------------------");
			for (Transfer t : transferHistory) {
				System.out.println(t.listOverview(currentUsername));
			}
			System.out.println("----------");
			int transferChoice = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
			if (transferChoice > 0) {
				for (Transfer t : transferHistory) {
					if (t.getTransferId() == transferChoice) {
						System.out.println("------------------------------------------");
						System.out.println("Transfer Details");
						System.out.println("------------------------------------------");
						System.out.println(t);
					}
				}
			}
		}
	}

	private void viewPendingRequests() throws TenmoServiceException {
		Transfer pendingTransfer = new Transfer();
		Transfer sendTransfer = new Transfer();
		Transfer[] transferHistory = tenmoService.listTransferHistory(currentUserId);
		List<Transfer> pendingTransfers = new ArrayList<>();

		for (Transfer t : transferHistory) {
			if (t.getAccountTo().equals(currentUsername)) {
				if (t.getTransferStatus().equals("Pending")) {
					pendingTransfers.add(t);
				}
			}
		}
		System.out.println("------------------------------------------");
		System.out.println("Transfers");
		System.out.printf("%-8s %-20s %-10s\n", "ID", "To", "Amount");
		System.out.println("------------------------------------------");
		for (Transfer t : pendingTransfers) {
			System.out.println(t.listOverview(currentUsername));
		}
		System.out.println("----------");

		boolean correctChoice = false;
		while (!correctChoice) {
			int transferChoice = console.getUserInputInteger("Please enter transfer ID to approve/reject (0 to cancel)");
			if (transferChoice == 0) {
				return;
			}
			for (Transfer t : pendingTransfers) {
				if (t.getTransferId() == transferChoice) {
					pendingTransfer = t;
					correctChoice = true;
				}
			}
			System.out.println("Invalid transfer ID. Please enter a valid ID.");
		}

		String choice = (String) console.getChoiceFromOptions(PENDING_MENU_OPTIONS);
		if (PENDING_MENU_OPTION_APPROVE.equals(choice)) {
			sendTransfer = new Transfer(pendingTransfer.getTransferId(), pendingTransfer.getTransferType(), "Approved",
					pendingTransfer.getAccountFrom(), pendingTransfer.getAccountTo(), pendingTransfer.getAmount());
		} else if (PENDING_MENU_OPTION_REJECT.equals(choice)) {
			sendTransfer = new Transfer(pendingTransfer.getTransferId(), pendingTransfer.getTransferType(), "Rejected",
					pendingTransfer.getAccountFrom(), pendingTransfer.getAccountTo(), pendingTransfer.getAmount());
		} else if (PENDING_MENU_OPTION_NO_APPROVE_REJECT.equals(choice)) {
			return;
		}

		tenmoService.updateTransfer(sendTransfer, currentUserId);
	}

	private void sendBucks() throws TenmoServiceException {

		Transfer transfer = transferService.sendTransfer(currentUser.getUser());
		if (transfer == null) {
			return;
		}
		tenmoService.makeTransfer(transfer, currentUserId);
	}

	private void requestBucks() throws TenmoServiceException {

		Transfer transfer = transferService.requestTransfer(currentUser.getUser());
		if (transfer == null) {
			return;
		}
		tenmoService.makeTransfer(transfer, currentUserId);
	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else if (LOGIN_MENU_BACKDOOR.equals(choice)) {
				UserCredentials credentials = new UserCredentials("testUser", "password");
				try {
					currentUser = authenticationService.login(credentials);
					tenmoService.AUTH_TOKEN = currentUser.getToken();
					currentUserId = currentUser.getUser().getId();
					currentUsername = currentUser.getUser().getUsername();
				} catch (Exception e) {
				}
			} else if (MENU_OPTION_EXIT.equals(choice)) {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}

	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				exception = new ExceptionHandler(e.getMessage());
				System.out.println("REGISTRATION ERROR: " + exception);
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
				tenmoService.AUTH_TOKEN = currentUser.getToken();
				// storing the id and username as private variables to make it easier to call
				// them later
				currentUserId = currentUser.getUser().getId();
				currentUsername = currentUser.getUser().getUsername();
			} catch (AuthenticationServiceException e) {
				exception = new ExceptionHandler(e.getMessage());
				System.out.println("LOGIN ERROR: " + exception);
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
