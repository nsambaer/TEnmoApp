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
import org.springframework.jdbc.support.rowset.SqlRowSet;

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
	private Transfer transfer3;
	private Transfer transfer4;
	private JdbcTemplate jdbc;
	
	
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
	jdbc = new JdbcTemplate(dataSource);
	
	//clean db
	String[] sqlDelete= {"DELETE FROM transfers", "DELETE FROM accounts", "DELETE FROM users"};
	for (String sql: sqlDelete) {
	jdbc.update(sql);
	}
	
	//add test objects
	user1 = new User(15l, "Hall", "123456789abcdefg", "user");
	user2 = new User(16l, "Oates", "123456789abcdefg", "user");
	
	String sqlAddUser = "INSERT INTO users (user_id, username, password_hash) "
					  + "VALUES (?,?,?)";
	jdbc.update(sqlAddUser, user1.getId(), user1.getUsername(), user1.getPassword());
	jdbc.update(sqlAddUser, user2.getId(), user2.getUsername(), user2.getPassword());
	
	
	account1 = new Account(USER_ONE, USER_ONE, BigDecimal.valueOf(100.00));
	account2 = new Account(USER_TWO, USER_TWO, BigDecimal.valueOf(200.00));
	
	String sqlAddAccount = "INSERT INTO accounts (account_id, user_id, balance) VALUES (?,?,?)";
	jdbc.update(sqlAddAccount, account1.getAccountId(), account1.getUserId(), account1.getBalance());
	jdbc.update(sqlAddAccount, account2.getAccountId(), account2.getUserId(), account2.getBalance());
	
	transfer1 = new Transfer(20, "Send", "Approved", "Hall", "Oates", BigDecimal.TEN);
	transfer2 = new Transfer(21, "Send", "Approved", "Oates", "Hall", BigDecimal.TEN);
	transfer3 = new Transfer(22, "Request", "Pending", "Hall", "Oates", BigDecimal.TEN);
	transfer4 = new Transfer(23, "Request", "Rejected", "Oates", "Hall", BigDecimal.TEN);
	
	
	String sqlAddTransfer = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) "
			+ "VALUES (?,?,?,?,?,?)";
	jdbc.update(sqlAddTransfer, transfer1.getTransferId(), 2, 2, USER_ONE, USER_TWO, transfer1.getAmount());
	jdbc.update(sqlAddTransfer, transfer2.getTransferId(), 2, 2, USER_TWO, USER_ONE, transfer2.getAmount());
	jdbc.update(sqlAddTransfer, transfer3.getTransferId(), 1, 1, USER_ONE, USER_TWO, transfer3.getAmount());
	jdbc.update(sqlAddTransfer, transfer4.getTransferId(), 1, 3, USER_TWO, USER_ONE, transfer4.getAmount());
	
	
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
		expectedList.add(transfer3);
		expectedList.add(transfer4);
		
		List<Transfer> testList = tDAO.getTransferHistoryByUserId(USER_ONE);
		
		assertEquals(expectedList.size(), testList.size());
		for(int x = 0; x < testList.size(); x++) {
			assertTransferEquals(expectedList.get(x), testList.get(x));
		}
		
	}
	
	@Test
	public void create_transfer_test() {
		int id = 100;
		Transfer expected = new Transfer(id, "Send", "Approved", "Hall", "Oates", BigDecimal.valueOf(15.00));
		
		expected = tDAO.createTransfer(expected);
		
		//complicated selects needed to fish out the correct strings from the database
		String sqlSelect = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, uf.username AS account_from, ut.username as account_to, amount " 
				+ "FROM transfers t " 
				+ "INNER JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " 
				+ "INNER JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id "
				+ "INNER JOIN accounts af ON t.account_from = af.account_id " //accounts is joined twice, once for each account field in transfers 
				+ "INNER JOIN accounts at ON t.account_to = at.account_id " 
				+ "INNER JOIN users uf ON af.user_id = uf.user_id " //users is joined twice, once for each accounts join above 
				+ "INNER JOIN users ut ON at.user_id = ut.user_id " 
				+ "WHERE transfer_id = ?";
		SqlRowSet results = jdbc.queryForRowSet(sqlSelect, expected.getTransferId());
		Transfer actual = new Transfer();
		if (results.next()) {
			actual = mapRowToTransfer(results);
		}		
		assertTransferEquals(expected, actual);
	}

	@Test
	public void update_transfer_test() {
		transfer3.setTransferStatus("Approved");
		tDAO.updateTransfer(transfer3);
		
		//complicated selects needed to fish out the correct strings from the database
		String sqlSelect = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, uf.username AS account_from, ut.username as account_to, amount " 
				+ "FROM transfers t " 
				+ "INNER JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " 
				+ "INNER JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id "
				+ "INNER JOIN accounts af ON t.account_from = af.account_id " //accounts is joined twice, once for each account field in transfers 
				+ "INNER JOIN accounts at ON t.account_to = at.account_id " 
				+ "INNER JOIN users uf ON af.user_id = uf.user_id " //users is joined twice, once for each accounts join above 
				+ "INNER JOIN users ut ON at.user_id = ut.user_id " 
				+ "WHERE transfer_id = ?";
		SqlRowSet results = jdbc.queryForRowSet(sqlSelect, transfer3.getTransferId());
		Transfer actual = new Transfer();
		if (results.next()) {
			actual = mapRowToTransfer(results);
		}
		assertTransferEquals(transfer3, actual);
	}
	
	

	private void assertTransferEquals(Transfer expected, Transfer actual) {
		assertEquals(expected.getTransferId(), actual.getTransferId());
		assertEquals(expected.getTransferType(), actual.getTransferType());
		assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
		assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
		assertEquals(expected.getAccountTo(), actual.getAccountTo());
		assertEquals(expected.getAmount(), actual.getAmount());
	}

	private Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer transfer = new Transfer();
		transfer.setTransferId(results.getInt("transfer_id"));
		transfer.setTransferType(results.getString("transfer_type_desc"));
		transfer.setTransferStatus(results.getString("transfer_status_desc"));
		transfer.setAccountFrom(results.getString("account_from"));
		transfer.setAccountTo(results.getString("account_to"));
		transfer.setAmount(results.getBigDecimal("amount"));
		return transfer;
	}














}
