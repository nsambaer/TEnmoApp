package com.techelevator.tenmo.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.view.ConsoleService;

public class TenmoService {

	private final String BASE_URL;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ConsoleService console = new ConsoleService(System.in, System.out);
	public static String AUTH_TOKEN = "";

	public TenmoService(String BASE_URL) {
		this.BASE_URL = BASE_URL;
	}

	public Account listCurrentBalance(int userId) {
		Account account = null;

		account = restTemplate.exchange(BASE_URL + "account/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class)
				.getBody();

		return account;
	}

	public Transfer[] listTransferHistory(int userId) {
		Transfer[] transferHistory = null;

		transferHistory = restTemplate.exchange(BASE_URL + "account/" + userId + "/transfers", HttpMethod.GET,
				makeAuthEntity(), Transfer[].class).getBody();

		return transferHistory;
	}
	
	public List<User> viewAllUsers() {
		User [] users = null;
		
		users = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
		return Arrays.asList(users);
	}
	
	

	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
}
