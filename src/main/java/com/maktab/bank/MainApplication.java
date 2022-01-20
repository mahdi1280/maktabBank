package com.maktab.bank;

import com.maktab.bank.model.Bank;
import com.maktab.bank.model.Customer;
import com.maktab.bank.model.Wallet;
import com.maktab.bank.service.BankService;
import com.maktab.bank.service.BankServiceImpl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class MainApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static Customer customer;

    public static void main(String[] args) throws SQLException {

        int amount;
        String password;
        long walletId;

        BankServiceImpl bank = new BankService();
        showMenu();
        boolean state = true;
        while (state) {
            switch (scanner.nextInt()) {
                case 1:
                    customer = bank.saveCustomer(createCustomer());
                    System.out.println("OK");
                    break;
                case 2:
                    customer = bank.login(createCustomer());
                    System.out.println("OK");

                    break;
                case 3:
                    bank.showBankList();
                    System.out.print("please select the bank: ");
                    long bankId = scanner.nextInt();
                    bank.saveWallet(createWallet(bankId));
                    System.out.println("OK");

                    break;
                case 4:
                    scanner.nextLine();
                    System.out.print("please enter walletId: ");
                    walletId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("please enter password: ");
                    password = scanner.nextLine();
                    System.out.print("please enter amount: ");
                    amount = scanner.nextInt();
                    bank.deposit(walletId, customer.getId(), password, amount);
                    System.out.println("OK");

                    break;
                case 5:
                    scanner.nextLine();
                    System.out.print("please enter walletId: ");
                    walletId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("please enter password: ");
                    password = scanner.nextLine();
                    System.out.print("please enter amount: ");
                    amount = scanner.nextInt();
                    bank.withdraw(walletId, customer.getId(), password, amount);
                    System.out.println("OK");

                    break;
                case 6:
                    scanner.nextLine();
                    System.out.print("please enter walletId: ");
                    walletId = scanner.nextLong();
                    System.out.print("please enter other walletId: ");
                    long otherWalletId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("please enter password: ");
                    password = scanner.nextLine();
                    System.out.print("please enter amount: ");
                    amount = scanner.nextInt();
                    bank.cardToCard(walletId, otherWalletId, amount, password);
                    System.out.println("OK");

                    break;
                case 7:
                    scanner.nextLine();
                    System.out.print("please enter walletId: ");
                    walletId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("please enter password: ");
                    password = scanner.nextLine();
                    System.out.println("amount: "+bank.getAmount(walletId, password));
                    System.out.println("OK");

                    break;
                case 8:
                    scanner.nextLine();
                    System.out.print("please enter walletId: ");
                    walletId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("please enter password: ");
                    password = scanner.nextLine();
                    bank.showListTransaction(walletId, customer.getId(), password);
                    System.out.println("OK");

                    break;
                case 9:
                    bank.showBankList();
                    break;
                case 10:
                    scanner.nextLine();
                    System.out.print("please enter walletId: ");
                    walletId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("please enter password: ");
                    password = scanner.nextLine();
                    System.out.print("please enter new password: ");
                    String newPassword = scanner.nextLine();
                    bank.changePassword(walletId, password,newPassword);
                    System.out.println("OK");
                    break;
                case 11:
                    state = false;
                    break;

            }
        }

    }

    private static Wallet createWallet(long bankId) {
        scanner.nextLine();
        System.out.print("enter the cvv2: ");
        String cvv2 = scanner.nextLine();
        System.out.print("enter the expire date: ");
        String expireDate = scanner.nextLine();
        System.out.print("enter the password: ");
        String password = scanner.nextLine();
        return Wallet.builder()
                .setCvv2(cvv2)
                .setExpirationDate(Date.valueOf(expireDate))
                .setPassword(password)
                .setCustomer(customer)
                .setBank(new Bank(bankId))
                .build();
    }

    private static Customer createCustomer() {
        scanner.nextLine();
        System.out.print("please enter user name: ");
        String username = scanner.nextLine();
        System.out.print("please enter password: ");
        String password = scanner.nextLine();
        return Customer.builder()
                .setUserName(username)
                .setPassword(password)
                .build();
    }

    private static void showMenu() {
        System.out.println("***please first login or create user***** ");
        System.out.println("1.create user");
        System.out.println("2.login");
        System.out.println("3.create wallet");
        System.out.println("4.deposit");
        System.out.println("5.withdraw");
        System.out.println("6.card to card");
        System.out.println("7.get amount");
        System.out.println("8.show list transaction");
        System.out.println("9.show bank");
        System.out.println("10.change password");
        System.out.println("11.exit");
    }
}
