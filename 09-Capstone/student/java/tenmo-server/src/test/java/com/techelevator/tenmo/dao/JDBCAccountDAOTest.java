package com.techelevator.tenmo.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Account;

public class JDBCAccountDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCAccountDAO accountDAO;
	private UserDAO uDAO;
	private static final int USER_ID = 100;
	private Account expectedAccount;

	@BeforeClass
	public static void setUpDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void tearDownAfterClass() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setup() {
		expectedAccount = new Account(100, USER_ID, BigDecimal.valueOf(1000));
		accountDAO = new JDBCAccountDAO(dataSource, uDAO);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String sqlAddUser = "INSERT INTO users (user_id, username, password_hash) "
						  + "VALUES (?, 'Han Solo', '123456789qwerty')";
		jdbcTemplate.update(sqlAddUser, USER_ID);
		String sqlAddAccount = "INSERT INTO accounts (account_id, user_id, balance) " + "VALUES (?, ?, ?)";
		jdbcTemplate.update(sqlAddAccount, expectedAccount.getAccountId(), USER_ID, expectedAccount.getBalance());
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void getAccountByUserId() {

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Account actualAccount = accountDAO.getAccountByUserId(USER_ID);

		assertNotNull(actualAccount);
		assertAccountEquals(expectedAccount, actualAccount);
	}
	
	@Test
	public void updateAccountBalance() throws OverdraftException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String username = "Han Solo";
		BigDecimal originalBalance = BigDecimal.valueOf(1000);
		BigDecimal amount = BigDecimal.valueOf(10);
		BigDecimal updatedBalance = BigDecimal.valueOf(990);
		
		Account savedAccount = accountDAO.updateAccountBalance(expectedAccount.getUserId(), amount);
		
//			Account actualAccount = accountDAO.updateAccountBalance("Han Solo", BigDecimal.valueOf(1));
//
//			assertAccountEquals(expectedAccount, actualAccount);
		
		
	}
	
	
	private Account getAccount(int accountId, int userId, BigDecimal balance) {
		Account theAccount = new Account();
		theAccount.setAccountId(accountId);
		theAccount.setUserId(userId);
		theAccount.setBalance(balance);
		return theAccount;
	}
	
	private void assertAccountEquals(Account expected, Account actual) {
		assertEquals(expected.getAccountId(), actual.getAccountId());
		assertEquals(expected.getUserId(), actual.getUserId());
		assertEquals(expected.getBalance(), actual.getBalance());
	}

}
