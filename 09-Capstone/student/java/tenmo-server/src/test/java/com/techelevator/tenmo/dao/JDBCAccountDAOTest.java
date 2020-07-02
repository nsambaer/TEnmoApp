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
import com.techelevator.tenmo.model.User;

public class JDBCAccountDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCAccountDAO accountDAO;
	private UserDAO uDAO;
	private User user1;
	private static final int USER_ONE = 15;
	private User user2;
	private static final int USER_TWO = 16;
	private Account account1;
	private Account account2;
//	private static final int USER_ID = 100;
//	private Account expectedAccount;

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
		accountDAO = new JDBCAccountDAO(dataSource, uDAO);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		//clean db
		String[] sqlDelete= {"DELETE FROM transfers", "DELETE FROM accounts", "DELETE FROM users"};
		for (String sql: sqlDelete) {
		jdbcTemplate.update(sql);
		}
		
		//add test objects
		user1 = new User(15l, "Hall", "123456789abcdefg", "user");
		user2 = new User(16l, "Oates", "123456789abcdefg", "user");
		
		String sqlAddUser = "INSERT INTO users (user_id, username, password_hash) "
						  + "VALUES (?,?,?)";
		jdbcTemplate.update(sqlAddUser, user1.getId(), user1.getUsername(), user1.getPassword());
		jdbcTemplate.update(sqlAddUser, user2.getId(), user2.getUsername(), user2.getPassword());
		
		
		account1 = new Account(USER_ONE, USER_ONE, BigDecimal.valueOf(100.00));
		account2 = new Account(USER_TWO, USER_TWO, BigDecimal.valueOf(200.00));
		
		String sqlAddAccount = "INSERT INTO accounts (account_id, user_id, balance) VALUES (?,?,?)";
		jdbcTemplate.update(sqlAddAccount, account1.getAccountId(), account1.getUserId(), account1.getBalance());
		jdbcTemplate.update(sqlAddAccount, account2.getAccountId(), account2.getUserId(), account2.getBalance());

//		String sqlAddUser = "INSERT INTO users (user_id, username, password_hash) "
//						  + "VALUES (?, 'Han Solo', '123456789qwerty')";
//		jdbcTemplate.update(sqlAddUser, USER_ID);
//		String sqlAddAccount = "INSERT INTO accounts (account_id, user_id, balance) " + "VALUES (?, ?, ?)";
//		jdbcTemplate.update(sqlAddAccount, expectedAccount.getAccountId(), USER_ID, expectedAccount.getBalance());
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void getAccountByUserIdTest() {

		Account actualAccount = accountDAO.getAccountByUserId(USER_ONE);

		assertNotNull(actualAccount);
		assertAccountEquals(account1, actualAccount);
	}
	
	@Test
	public void updateAccountBalanceTest() throws OverdraftException {

		BigDecimal amount = BigDecimal.valueOf(200);
		
		account1.setBalance(BigDecimal.valueOf(300.00));
		accountDAO.updateAccountBalance(user1.getUsername(), amount);
		Account testAccount = accountDAO.getAccountByUserId(USER_ONE);
		
		assertAccountEquals(account1, testAccount);
		
		
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
