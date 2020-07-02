package com.techelevator.tenmo.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
	
	//add 
	String sqlAddUser = "INSERT INTO users (user_id, username, password_hash) "
					  + "VALUES (?, 'Han Solo', '123456789qwerty')";
	jdbcTemplate.update(sqlAddUser, USER_ID);
	String sqlAddAccount = "INSERT INTO accounts (account_id, user_id, balance) "
						 + "VALUES (100, ?, 1000)";
	jdbcTemplate.update(sqlAddAccount, USER_ID);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void getAccountByUserId() {

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM accounts WHERE user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, USER_ID);
		results.next();
		
		Account account = accountDAO.getAccountByUserId(USER_ID);
		
		assertNotNull(account);
		assertEquals(results.getInt(2), account.getUserId());
		assertAccountEquals(results, account);
	}
	
	private void assertAccountEquals(Account expected, Account actual) {
		assertEquals(expected.getAccountId(), actual.getAccountId());
		assertEquals(expected.getUserId(), actual.getUserId());
		assertEquals(expected.getBalance(), actual.getBalance());
	}

}
