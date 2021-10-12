package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

@Component
public class JdbcAccountDao implements AccountDao{
    private final JdbcTemplate jdbcTemplate;

    private final Logger LOGGER = Logger.getLogger(JdbcAccountDao.class.getCanonicalName());

    public JdbcAccountDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public Account createAccount(Account account) {

        String sql = "INSERT INTO accounts (user_id, balance) VALUES(?, ?) ;";
        jdbcTemplate.update( sql, account.getUser_Id(), account.getBalance().doubleValue());


        return getAccount(findAccountIdByUserId(account.getUser_Id()));
    }
    @Override
    public long findAccountIdByUserId(long userId) {
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?";
        LOGGER.info(" USING SQL TO FIND USER INFORMATION " + sql + " USER ID " + userId);
        Long id = jdbcTemplate.queryForObject(sql, Long.class, userId);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }


    @Override
    public void updateAccount(Account account, int id) {
        String sql = "UPDATE accounts " +
                "SET  user_id = ?, balance = ? " +
                "WHERE account_id = ?";
        jdbcTemplate.update(sql, account.getUser_Id(), account.getBalance(), account.getAccount_Id());
    }

    @Override
    public Account getAccount( long account_Id) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE account_id = ? ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_Id);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public BigDecimal getAccountBalance( int user_Id) {
        BigDecimal balance = null;
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = null;
        try{
            results = jdbcTemplate.queryForRowSet(sql, user_Id);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        }catch (DataAccessException e){
            System.out.println("Error accessing data!");
        }

        return balance;
    }
    //Increases the user balance
    @Override
    public BigDecimal increaseBalance(BigDecimal addedAmount, int id) {
        Account account = getAccount(id);
        BigDecimal newAccountBalance = account.getBalance().add(addedAmount);
        System.out.println(newAccountBalance);
        String sqlString = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sqlString, newAccountBalance, id);
        } catch (DataAccessException e) {
            System.out.println("Error accessing the data");
        }
        return account.getBalance();
    }
    //Decreases the users balance
    @Override
    public BigDecimal decreaseBalance(BigDecimal subtractedAmount, int id) {
        Account account = getAccount(id);
        BigDecimal newBalance = account.getBalance().subtract(subtractedAmount);
        System.out.println(newBalance);
        String sqlString = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sqlString, newBalance, id);
        } catch (DataAccessException e) {
            System.out.println("Error accessing the data");
        }
        return account.getBalance();
    }

    private Account mapRowToAccount(SqlRowSet rs){
        Account account = new Account();
        account.setAccount_Id(rs.getLong("account_id"));
        account.setUser_Id(rs.getLong("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));

        return account;
    }


}
