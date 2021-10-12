package com.techelevator.tenmo.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Account {
    @NotNull(message = "This should not be empty")
    private long account_Id;
    private long user_Id;

    //Constructor
    public Account(@NotNull long account_Id, @NotNull long user_Id, @Positive BigDecimal balance) {
        this.account_Id = account_Id;
        this.user_Id = user_Id;
        this.balance = balance;
    }

    @Positive(message = "This balance should be greater than 0")
    private BigDecimal balance;

    public Account() {

    }

    public long getAccount_Id() {
        return account_Id;
    }

    public void setAccount_Id(long account_Id) {
        this.account_Id = account_Id;
    }

    public long getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(long user_Id) {
        this.user_Id = user_Id;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
