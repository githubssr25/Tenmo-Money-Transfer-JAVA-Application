package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    Transfer getTransfer(int id);

    Transfer createTransfer(Transfer transfer);

    List<Transfer> searchByTransferType(int id);

    String sendTransferTo(int user_From, int user_To, BigDecimal amount);

    int getTransferByAccountAndAmount(double account_from, double account_to, double amount);

    Transfer[] getTransfersByUserId(int id, int account_type);

}
