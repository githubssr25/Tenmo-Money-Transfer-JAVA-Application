package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    Account createAccount(Account account);

    void updateAccount(Account account, int id);
        Account getAccount(long id);
    BigDecimal getAccountBalance(int id);
    BigDecimal increaseBalance(BigDecimal addedAmount, int id);
    BigDecimal decreaseBalance(BigDecimal subtractedAmount, int id);
    long findAccountIdByUserId(long userId);
}
