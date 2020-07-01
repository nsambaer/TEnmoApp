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

	private JdbcTemplate jdbc;
	public JDBCTransferDAO(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Transfer> getTransferHistoryByUserId(int userId) {
		
		List<Transfer> transferHistory = new ArrayList<>();
		String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount "
				+ "FROM transfers t "
				+ "INNER JOIN transfer_types tt "
				+ "ON t.transfer_type_id = tt.transfer_type_id "
				+ "INNER JOIN transfer_statuses ts "
				+ "ON t.transfer_status_id = ts.transfer_status_id "
				+ "WHERE account_from = ?";
		SqlRowSet results = jdbc.queryForRowSet(sql, userId);
		
		while(results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transferHistory.add(transfer);
		}
		
		return transferHistory;
	}
	
	@Override
	public Transfer createTransfer(Transfer transfer) {
		
		String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " + 
					"VALUES ((SELECT transfer_type_id FROM transfer_types tt WHERE tt.transfer_type_desc = ?), "
				+ "(SELECT transfer_status_id FROM transfer_statuses ts WHERE ts.transfer_status_desc = ?), ?, ?, ?) "
				+ "RETURNING transfer_id";
		int transferId = jdbc.queryForObject(sql, Integer.class, transfer.getTransferType(), transfer.getTransferStatus(), transfer.getAccountFromId(), 
												transfer.getAccountToId(), transfer.getAmount());
		transfer.setTransferId(transferId);
		return transfer;
	}

	
	private Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer transfer = new Transfer();
		transfer.setTransferId(results.getInt("transfer_id"));
		transfer.setTransferType(results.getString("transfer_type_desc"));
		transfer.setTransferStatus(results.getString("transfer_status_desc"));
		transfer.setAccountFromId(results.getInt("account_from"));
		transfer.setAccountToId(results.getInt("account_to"));
		transfer.setAmount(results.getBigDecimal("amount"));
		return transfer;
	}

}
