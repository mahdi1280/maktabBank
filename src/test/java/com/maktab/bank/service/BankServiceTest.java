package com.maktab.bank.service;

import com.maktab.bank.model.Bank;
import com.maktab.bank.model.Customer;
import com.maktab.bank.model.Wallet;
import com.maktab.bank.repository.MySession;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

class BankServiceTest {

    private final BankService bankService = new BankService();

    BankServiceTest() throws SQLException {
    }

    @Test
    void testConnection() {
        Session session = MySession.getSession();
    }

    @Test
    void saveCustomer() {
        Customer customer = Customer.builder()
                .setPassword("!23")
                .setUserName("123")
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .build();
        Customer customer1 = bankService.saveCustomer(customer);
        assertNull(customer1);
    }

    @Test
    void saveCustomerAndCheckBody() throws SQLException {
        Customer customer = Customer.builder()
                .setPassword("!23")
                .setUserName("123")
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .build();
        Customer customer1 = bankService.saveCustomer(customer);
        assertDoesNotThrow(() -> bankService.login(customer1));
    }

    @Test
    void showBankList() {
        try {
            bankService.showBankList();
        } catch (SQLException e) {
            assert false;
        }
    }

    @Test
    void saveWallet() {
        Wallet wallet = Wallet.builder()
                .setPassword("Asda")
                .setBank(new Bank(1))
                .setCustomer(new Customer(1))
                .setExpirationDate(Date.valueOf(LocalDate.now()))
                .build();
        try {
            bankService.saveWallet(wallet);
        } catch (SQLException e) {
            assert false;
        }
    }

    @Test
    void changePasswordWalletNotFound() throws SQLException {
        bankService.changePassword(1,"asd","asda");
    }

    @Test
    void changePasswordPasswordNotValid() throws SQLException {
        bankService.changePassword(6,"asd","asda");
    }

    @Test
    void saveDepositWalletPasswordNotValid() throws SQLException {
        bankService.deposit(1,1,"asd",12);
    }

    @Test
    void saveDepositWalletNotUser() throws SQLException {
        bankService.deposit(2,1,"asd",12);
    }

    @Test
    void saveDepositWalletNormal() throws SQLException {
        bankService.deposit(1000000000004L,1,"karimi",1);
    }
}