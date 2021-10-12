package com.techelevator.tenmo.model;

import java.math.BigDecimal;


public class Transfer {
    private long transfer_Id;
    private long transfer_Type_Id;
    private long transfer_Status_Id;
    private long account_from;



    private double account_to;
    private String user_From;
    private String user_To;
    private String transfer_Status_desc;
    private String transfer_Type_desc;
    private double amount;




    public String getUser_From() {
        return user_From;
    }

    public void setUser_From(String user_From) {
        this.user_From = user_From;
    }

    public String getUser_To() {
        return user_To;
    }

    public void setUser_To(String user_To) {
        this.user_To = user_To;
    }

    public Transfer(){};
    //Constructor
    public Transfer( long transfer_Id, long transfer_Type_Id, long transfer_Status_Id, long account_from,
                    double account_to, String user_From, String user_To, double amount) {
        this.transfer_Id = transfer_Id;
        this.transfer_Type_Id = transfer_Type_Id;
        this.transfer_Status_Id = transfer_Status_Id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.user_From = user_From;
        this.user_To = user_To;
        this.amount = amount;
    }



    public long getTransfer_Id() {
        return transfer_Id;
    }

    public void setTransfer_Id(int transfer_Id) {
        this.transfer_Id = transfer_Id;
    }

    public long getTransfer_Type_Id() {
        return transfer_Type_Id;
    }

    public void setTransfer_Type_Id(int transfer_Type_Id) {
        this.transfer_Type_Id = transfer_Type_Id;
    }

    public long getTransfer_Status_Id() {
        return transfer_Status_Id;
    }

    public void setTransfer_Status_Id(int transfer_Status_Id) {
        this.transfer_Status_Id = transfer_Status_Id;
    }

    public double getAccount_from() {
        return account_from;
    }

    public void setAccount_from(long account_from) {
        this.account_from = account_from;
    }

    public double getAccount_to() {
        return account_to;
    }

    public void setAccount_to(long amount_to) {
        this.account_to = amount_to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public String getTransfer_Status_desc() {
        return transfer_Status_desc;
    }

    public void setTransfer_Status_desc(String transfer_Status_desc) {
        this.transfer_Status_desc = transfer_Status_desc;
    }
    public void setTransfer_Type_desc(String transfer_type_desc) {
        this.transfer_Type_desc = transfer_type_desc;
    }

    public String getTransfer_Type_desc() {
        return transfer_Type_desc;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transfer_Id=" + transfer_Id +
                ", transfer_Type_Id=" + transfer_Type_Id +
                ", transfer_Status_Id=" + transfer_Status_Id +
                ", account_from=" + account_from +
                ", account_to=" + account_to +
                ", user_From='" + user_From + '\'' +
                ", user_To='" + user_To + '\'' +
                ", transfer_Status_desc='" + transfer_Status_desc + '\'' +
                ", transfer_Type_desc='" + transfer_Type_desc + '\'' +
                ", amount=" + amount +
                '}';
    }
}


