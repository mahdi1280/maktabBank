package com.maktab.bank.service;

import com.maktab.bank.customexception.*;
import com.maktab.bank.model.*;
import com.maktab.bank.model.enumType.TransactionType;
import com.maktab.bank.repository.BankRepository;
import com.maktab.bank.repository.CustomerRepository;
import com.maktab.bank.repository.TransactionRepository;
import com.maktab.bank.repository.WalletRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BankService implements BankServiceImpl {

    private static Connection connection;

    static {
        try {
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private final BankRepository bankRepository=new BankRepository();
    private final WalletRepository walletRepository = new WalletRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    public BankService() throws SQLException {
       Bank bank = new Bank(connection);
       Wallet wallet = new Wallet(connection);
       Transaction transaction = new Transaction(connection);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        customer.setCreatedAt(Date.valueOf(LocalDate.now()));
        return customerRepository.save(customer);
    }

    @Override
    public void saveWallet(Wallet wallet) throws SQLException {
        walletRepository.save(connection, wallet);
    }

    @Override
    public void deposit(long walletId, long customerId, String password, int amount) throws SQLException {
       Customer customer;
       Wallet wallet;
        try {
            wallet=walletRepository.find(connection, walletId, password);
            customer=customerRepository.find(customerId);
        } catch (NotFoundWalletException | PasswordException | CustomerNotFoundException notFoundWalletException) {
            System.out.println(notFoundWalletException.getMessage());
            return;
        }
        Transaction transaction = Transaction.builder()
                .setDate(Date.valueOf(LocalDate.now()))
                .setPrice(amount)
                .setType(TransactionType.DEPOSIT)
                .setCustomer(customer)
                .setWallet(wallet)
                .build();
        transactionRepository.save(connection, transaction);
    }

    @Override
    public void withdraw(long walletId,long customerId, String password, int amount) throws SQLException {
        Wallet wallet;
        Customer customer;
        try {
           wallet= walletRepository.find(connection, walletId, password);
           customer= customerRepository.find(customerId);
        } catch (NotFoundWalletException | CustomerNotFoundException notFoundWalletException) {
            System.out.println(notFoundWalletException.getMessage());
            return;
        }
        int deposit=transactionRepository.getDeposit(connection,wallet.getId());
        int withdraw=transactionRepository.getWithdraw(connection,wallet.getId());
        if(deposit-withdraw-amount<0)
            throw new ManyException("not enugh many");
        Transaction transaction = Transaction.builder()
                .setDate(Date.valueOf(LocalDate.now()))
                .setPrice(amount)
                .setType(TransactionType.WITHDRAW)
                .setCustomer(customer)
                .setWallet(wallet)
                .build();
        transactionRepository.save(connection, transaction);
    }

    @Override
    public void cardToCard(long myWalletId, long otherWalletId, int amount, String password) throws SQLException {
       try {
           transactionRepository.cardToCard(connection, myWalletId, password, otherWalletId, amount);
       }catch (TryPasswordException | NotFoundWalletException | ManyException | PasswordException tryPasswordException){
           System.out.println(tryPasswordException.getMessage());
       }
    }

    @Override
    public long getAmount(long myWallet, String password) throws SQLException {
        Wallet wallet;
        try {
            wallet= walletRepository.find(connection, myWallet, password);
        }catch (PasswordException | NotFoundWalletException passwordException) {
            System.out.println(passwordException.getMessage());
            return 0;
        }
        int deposit = transactionRepository.getDeposit(connection, wallet.getId());
        int withdraw=transactionRepository.getWithdraw(connection,wallet.getId());
        return deposit-withdraw;
    }

    @Override
    public void showListTransaction(long myWallet,long userId, String password) throws SQLException {
        try {
        walletRepository.find(connection,myWallet,password);

        }catch (PasswordException | NotFoundWalletException passwordException){
            System.out.println(passwordException.getMessage());
            return;
        }
        List<Transaction> buUserId = transactionRepository.findBuUserId(connection, userId, myWallet);
        for (Transaction transaction : buUserId) {
            System.out.println(transaction.toString());
        }
    }

    @Override
    public void showBankList() throws SQLException {
        List<Bank> bankList = bankRepository.getBankList(connection);
        for (Bank bank : bankList) {
            System.out.println(bank.toString());
        }
    }

    @Override
    public Customer login(Customer customer) throws SQLException {
        try {
        return customerRepository.login(customer);

        }catch (CustomerNotFoundException customerNotFoundException){
            System.out.println(customerNotFoundException.getMessage());
            return null;
        }
    }

    @Override
    public void changePassword(long walletId, String password,String newPassword) throws SQLException {
        try{
        walletRepository.changePassword(connection,walletId,password,newPassword);
        }catch (PasswordException | NotFoundWalletException passwordException){
            System.out.println(passwordException.getMessage());
        }

    }
}
