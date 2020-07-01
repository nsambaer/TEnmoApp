package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;

@Component
public class JDBCAccountDAO implements AccountDAO {

	private JdbcTemplate jdbc;
	private UserDAO uDAO;
	
	public JDBCAccountDAO(DataSource dataSource, UserDAO userDAO) {
		this.jdbc = new JdbcTemplate(dataSource);
		this.uDAO = userDAO;
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
	public Account updateAccountBalance(String username, BigDecimal amount) {
		
		Account account = getAccountByUserId(uDAO.findIdByUsername(username));
		account.setBalance(account.getBalance().add(amount));
		String sql = "UPDATE accounts "
					+ "SET balance = ? "
					+ "WHERE user_id = ?";
		jdbc.update(sql, account.getBalance(), account.getAccountId());
		
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
