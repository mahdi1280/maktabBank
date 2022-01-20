package com.maktab.bank.service;

import com.maktab.bank.model.Customer;
import com.maktab.bank.model.Wallet;

import java.sql.SQLException;

public interface BankServiceImpl {

    Customer saveCustomer(Customer customer) throws SQLException;

    void saveWallet(Wallet wallet) throws SQLException;

    void deposit(long walletId, long customerId, String password, int amount) throws SQLException;

    void withdraw(long walletId,long customerId, String password, int amount) throws SQLException;

    void cardToCard(long myWalletId,long otherWalletId,int amount,String password) throws SQLException;

    long getAmount(long myWallet, String password) throws SQLException;

    void showListTransaction(long myWallet,long userId, String password) throws SQLException;

    void showBankList() throws SQLException;

    Customer login(Customer customer) throws SQLException;

    void changePassword(long walletId, String password,String newPassword) throws SQLException;
}
