package com.maktab.bank.service;

import com.maktab.bank.customexception.ManyException;
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
    private Bank bank;
    private Boss boss;
    private Customer customer;
    private Employee employee;
    private Transaction transaction;
    private Wallet wallet;

    public BankService() throws SQLException {
        customer = new Customer(connection);
        bank = new Bank(connection);
        wallet = new Wallet(connection);
        transaction = new Transaction(connection);
        employee = new Employee(connection);
        boss=new Boss(connection);
    }

    @Override
    public Customer saveCustomer(Customer customer) throws SQLException {
        customer.setCreatedAt(Date.valueOf(LocalDate.now()));
        return customerRepository.save(connection, customer);
    }

    @Override
    public void saveWallet(Wallet wallet) throws SQLException {
        walletRepository.save(connection, wallet);
    }

    @Override
    public void deposit(long walletId, long customerId, String password, int amount) throws SQLException {
        Wallet wallet = walletRepository.find(connection, walletId, password);
        Customer customer=customerRepository.find(connection,customerId);
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
        Wallet wallet = walletRepository.find(connection, walletId, password);
        Customer customer=customerRepository.find(connection,customerId);
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
        transactionRepository.cardToCard(connection,myWalletId,password,otherWalletId,amount);
    }

    @Override
    public long getAmount(long myWallet, String password) throws SQLException {
        Wallet wallet=walletRepository.find(connection,myWallet,password);
        int deposit=transactionRepository.getDeposit(connection,wallet.getId());
        int withdraw=transactionRepository.getWithdraw(connection,wallet.getId());
        return deposit-withdraw;
    }

    @Override
    public void showListTransaction(long myWallet,long userId, String password) throws SQLException {
        walletRepository.find(connection,myWallet,password);
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
        return customerRepository.login(connection,customer);
    }

    @Override
    public void changePassword(long walletId, String password,String newPassword) throws SQLException {
        walletRepository.changePassword(connection,walletId,password,newPassword);
    }
}
