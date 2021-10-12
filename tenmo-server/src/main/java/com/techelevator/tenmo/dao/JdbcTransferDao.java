package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JdbcTransferDao implements TransferDao{
    private final JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private final Logger LOGGER = Logger.getLogger(JdbcTransferDao.class.getCanonicalName());

    private JdbcTransferDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public Transfer getTransfer(int transfer_Id) {
        Transfer transfer = null;
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount, " +
                "tt.transfer_type_desc, ts.transfer_status_desc FROM transfers t " +
                "INNER JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id " +
                "INNER JOIN transfer_types tt ON tt.transfer_type_id = t.transfer_type_id  WHERE t.transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfer_Id);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }
    @Override
    public Transfer createTransfer(Transfer transfer) {

        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "+
                "VALUES (?, ?, ?, ?, ?)";
        LOGGER.info("For this transfer " + transfer);
        int rowsAffected =  jdbcTemplate.update(sql,
                transfer.getTransfer_Type_Id(),
                transfer.getAmount() >= 1000 ? 1 : 2,
                transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());

        if (rowsAffected == 1){
            return getTransfer(getTransferByAccountAndAmount(transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount()));
        }else {
            return null;
        }

    }

    @Override
    public int getTransferByAccountAndAmount(double account_from, double account_to, double amount) {

        String sql = "SELECT transfer_id FROM transfers WHERE account_from = ? and account_to = ? and amount = ?";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, account_from, account_to, amount);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }

    @Override
    public Transfer[] getTransfersByUserId(int account_from, int account_type) {
        return account_type == 1 ?  getTransfersByUserIdPending(account_from) : getTransfersByUserIdApproved(account_from);
    }


    public Transfer[] getTransfersByUserIdPending(int account_from) {

        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount, " +
                "tt.transfer_type_desc, ts.transfer_status_desc FROM transfers t " +
                "INNER JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id " +
                "INNER JOIN transfer_types tt ON tt.transfer_type_id = t.transfer_type_id AND t.transfer_status_id = 1 WHERE t.account_from = ?";
        Transfer[] transfers = null;
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_from);

        transfers = mapRowToTransferList(results);

        return transfers;
    }

    public Transfer[] getTransfersByUserIdApproved(int account_from) {


        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount, " +
                "tt.transfer_type_desc, ts.transfer_status_desc FROM transfers t " +
                "INNER JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id " +
                "INNER JOIN transfer_types tt ON tt.transfer_type_id = t.transfer_type_id AND t.transfer_status_id = 2 WHERE t.account_from = ?";
        Transfer[] transfers = null;
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_from);

        transfers = mapRowToTransferList(results);

        return transfers;
    }

    //User ability to look up a transfer by its type
    @Override
    public List<Transfer> searchByTransferType(int id) {
        String sql = "SELECT transfer_type_id "+
                "FROM Transfers"+
                "INNER JOIN account ON transfer.account_from = account.account_id " +
                "INNER Join user ON account.user_id = user.user_id "+
                "WHERE user_id = ?";
        return null;
    }
    //The transfer method from one user to another
    @Override
    public String sendTransferTo(int user_From, int user_To, BigDecimal amount) {
        if(user_From == user_To){
            return "Transfers to yourself are prohibited";
        }if (amount.compareTo(accountDao.getAccountBalance(user_From)) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (2, 2, ?, ?, ?)";
            jdbcTemplate.update(sql, user_From, user_To, amount);
            accountDao.increaseBalance(amount, user_To);
            accountDao.decreaseBalance(amount, user_From);
            return "Transfer complete! ";
        } else {
            return "Transfer has failed due to the [lack of funds] or the amount was less then or equal to 0 or not a Valid User";
        }
    }
    private Transfer mapRowToTransfer(SqlRowSet rs){
        Transfer transfer = new Transfer();
        transfer.setTransfer_Id(rs.getInt("transfer_id"));
        transfer.setTransfer_Type_Id(rs.getInt("transfer_type_id"));
        transfer.setTransfer_Status_Id(rs.getInt("transfer_status_id"));
        transfer.setTransfer_Type_desc(rs.getString("transfer_status_desc"));
        transfer.setTransfer_Status_desc(rs.getString("transfer_type_desc"));
        transfer.setAccount_from(rs.getLong("account_from"));
        transfer.setAccount_to(rs.getLong("account_to"));
        transfer.setAmount(rs.getDouble("amount"));
        return transfer;
    }
    private Transfer[] mapRowToTransferList(SqlRowSet rs){
        List<Transfer> transfers = new ArrayList<>();
        while (rs.next()){
            Transfer transfer = new Transfer();
            transfer.setTransfer_Id(rs.getInt("transfer_id"));
            transfer.setTransfer_Type_Id(rs.getInt("transfer_type_id"));
            transfer.setTransfer_Status_Id(rs.getInt("transfer_status_id"));
            transfer.setAccount_from(rs.getLong("account_from"));
            transfer.setAccount_to(rs.getLong("account_to"));
            transfer.setAmount(rs.getDouble("amount"));
            transfers.add(transfer);
        }

        return transfers.toArray(new Transfer[transfers.size()]);
    }
}