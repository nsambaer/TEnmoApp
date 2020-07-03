package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

@Component
public class JDBCTransferDAO implements TransferDAO {
	
	//more complicated SQL statements were preferred to allow us to store strings in our java objects
	//this means that if extra statuses or transfer types are added to the database we remain loosely coupled and should not have to modify anything
	//in addition, extra commands to fetch the account usernames are unnecessary
	
	private JdbcTemplate jdbc;

	public JDBCTransferDAO(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Transfer> getTransferHistoryByUserId(int userId) {

		List<Transfer> transferHistory = new ArrayList<>();
		// inner joins are to provide us with the Strings matching the ids in the
		// transfer type, transfer status, account from and account to fields in transfers
		String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, uf.username AS account_from, ut.username as account_to, amount "
				+ "FROM transfers t " 
				+ "INNER JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id "
				+ "INNER JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id "
				+ "INNER JOIN accounts af ON t.account_from = af.account_id " //accounts is joined twice, once for each account field in transfers
				+ "INNER JOIN accounts at ON t.account_to = at.account_id "
				+ "INNER JOIN users uf ON af.user_id = uf.user_id " //users is joined twice, once for each accounts join above
				+ "INNER JOIN users ut ON at.user_id = ut.user_id "
				+ "WHERE account_from = ? OR account_to = ? ORDER BY transfer_id";
		SqlRowSet results = jdbc.queryForRowSet(sql, userId, userId);

		while (results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transferHistory.add(transfer);
		}

		return transferHistory;
	}

	@Override
	public Transfer createTransfer(Transfer transfer) {
		// the transfers table expects a transfer type id, a transfer status id and
		// account ids, our java object stores those as strings
		// the select statements are used to provide the ids that match the strings in
		// our transfer object
		String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
				+ "VALUES ("
				+ "(SELECT transfer_type_id FROM transfer_types tt WHERE tt.transfer_type_desc = ?), "
				+ "(SELECT transfer_status_id FROM transfer_statuses ts WHERE ts.transfer_status_desc = ?), "
				+ "(SELECT account_id FROM accounts a INNER JOIN users u ON a.user_id = u.user_id WHERE username ILIKE ?), "
				+ "(SELECT account_id FROM accounts a INNER JOIN users u ON a.user_id = u.user_id WHERE username ILIKE ?), "
				+ "?) "
				+ "RETURNING transfer_id";
		int transferId = jdbc.queryForObject(sql, Integer.class, transfer.getTransferType(),
				transfer.getTransferStatus(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
		transfer.setTransferId(transferId);
		return transfer;
	}

	@Override
	public Transfer updateTransfer(Transfer transfer) {
		String sql = "UPDATE transfers "
				+ "SET transfer_status_id = (SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = ?) "
				+ "WHERE transfer_id = ?";
		jdbc.update(sql, transfer.getTransferStatus(), transfer.getTransferId());
		return transfer;
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
