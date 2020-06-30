package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Account;

public class JDBCAccountDAO implements AccountDAO {

	private JdbcTemplate jdbc;
	public JDBCAccountDAO(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Account getAccountByUserId(int userId) {

		Account account = new Account();
		String sql = "SELECT * FROM accounts WHERE user_id = ?";
		SqlRowSet results = jdbc.queryForRowSet(sql, userId);
		if (results.next()) {
			account = mapRowToAccount(results);
		}
		
		return account;
	}
	
	@Override
	public Account updateAccountBalance(int userId, BigDecimal amount) {
		
		Account account = getAccountByUserId(userId);
		account.setBalance(account.getBalance().add(amount));
		String sql = "UPDATE accounts "
					+ "SET balance = ? "
					+ "WHERE user_id = ?";
		jdbc.update(sql, account.getBalance(), account.getUserId());
		
		return account;		
	}
	
	
	
	private Account mapRowToAccount(SqlRowSet results) {
		Account account = new Account();
		account.setAccountId(results.getInt("account_id"));
		account.setUserId(results.getInt("user_id"));
		account.setBalance(results.getBigDecimal("balance"));
		
		return account;
	}
	

}
