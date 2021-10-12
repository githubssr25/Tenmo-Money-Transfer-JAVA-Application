package com.techelevator.tenmo.model;

public class Account {
        private int account_Id;
        private int user_Id;
        private double balance;
        public int getAccount_Id() {
            return account_Id;
        }
        public void setAccount_Id(int account_Id) {
            this.account_Id = account_Id;
        }
        public int getUser_Id() {
            return user_Id;
        }
        public void setUser_Id(int user_Id) {
            this.user_Id = user_Id;
        }
        public double getBalance() {
            return balance;
        }
        public void setBalance(double balance) {
            this.balance = balance;
        }

        public void setAuthorities(String user) {
        }



        public void setActivated(boolean b) {
        }
    }
