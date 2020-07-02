package com.techelevator.tenmo.dao;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

public class JDBCTransferDAOTest {


	private static SingleConnectionDataSource dataSource;
	private AccountDAO aDAO;
	private UserDAO uDAO;
	private TransferDAO tDAO;
	private User user1;
	private static final int USER_ONE = 15;
	private User user2;
	private static final int USER_TWO = 16;
	private Account account1;
	private Account account2;
	private Transfer transfer1;
	private Transfer transfer2;
	
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
	aDAO = new JDBCAccountDAO(dataSource, uDAO);
	tDAO = new JDBCTransferDAO(dataSource);
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
	
	transfer1 = new Transfer(20, "Send", "Approved", "Hall", "Oates", BigDecimal.TEN);
	transfer2 = new Transfer(21, "Send", "Approved", "Oates", "Hall", BigDecimal.TEN);
	
	String sqlAddTransfer = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) "
			+ "VALUES (?,?,?,?,?,?)";
	jdbcTemplate.update(sqlAddTransfer, transfer1.getTransferId(), 2, 2, USER_ONE, USER_TWO, transfer1.getAmount());
	jdbcTemplate.update(sqlAddTransfer, transfer2.getTransferId(), 2, 2, USER_TWO, USER_ONE, transfer2.getAmount());
	
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	
	@Test
	public void get_transfer_history_by_user_id_test() {
		List<Transfer> expectedList = new ArrayList<>();
		expectedList.add(transfer1);
		expectedList.add(transfer2);
		
		List<Transfer> testList = tDAO.getTransferHistoryByUserId(USER_ONE);
		
		assertEquals(expectedList.size(), testList.size());
		for(int x = 0; x < testList.size(); x++) {
			assertTransferEquals(expectedList.get(x), testList.get(x));
		}
		
	}
	
	@Test
	public void create_transfer_test() {
		Transfer expected = new Transfer(22, "Send", "Approved", "Hall", "Oates", BigDecimal.valueOf(15.00));
		
		Transfer actual = tDAO.createTransfer(expected);
		
		assertTransferEquals(expected, actual);
	}


	private void assertTransferEquals(Transfer expected, Transfer actual) {
		assertEquals(expected.getTransferId(), actual.getTransferId());
		assertEquals(expected.getTransferType(), actual.getTransferType());
		assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
		assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
		assertEquals(expected.getAccountTo(), actual.getAccountTo());
		assertEquals(expected.getAmount(), actual.getAmount());
	}
















}
