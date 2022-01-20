package com.maktab.bank.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Wallet {

    private long id;
    private String cvv2;
    private Date expirationDate;
    private String password;
    private int tryPassword;
    private Customer customer;
    private Bank bank;

    public Wallet(int tryPassword,long id, String cvv2, Date expirationDate, String password, Customer customer, Bank bank) {
        this.id = id;
        this.cvv2 = cvv2;
        this.expirationDate = expirationDate;
        this.password = password;
        this.customer = customer;
        this.bank = bank;
        this.tryPassword=tryPassword;
    }

    public Wallet(long id) {
        this.id = id;
    }

    public Wallet(Connection connection) throws SQLException {
        String sql="CREATE SEQUENCE if not exists bank.wallet_sec START 1000000000000;\n" +
                "CREATE TABLE IF NOT EXISTS bank.wallet(\n" +
                "    id bigint primary key ,\n" +
                "    cvv2 varchar(200),\n" +
                "    expirationDate date,\n" +
                "    password varchar(200),\n" +
                "    customer_id int,\n" +
                "    bank_id int,try_password int default 0,\n" +
                "    CONSTRAINT fk_customer_id FOREIGN KEY (customer_id)\n" +
                "        REFERENCES bank.customer (id),\n" +
                "    CONSTRAINT fk_bank_id FOREIGN KEY (bank_id)\n" +
                "        REFERENCES bank.bank (id)\n" +
                ");";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    public static Builder builder(){
        return new Builder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTryPassword() {
        return tryPassword;
    }

    public void setTryPassword(int tryPassword) {
        this.tryPassword = tryPassword;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public static class Builder{

        private long id;
        private String cvv2;
        private Date expirationDate;
        private String password;
        private int tryPassword;

        private Customer customer;
        private Bank bank;

        public Builder() {
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setCvv2(String cvv2) {
            this.cvv2 = cvv2;
            return this;
        }

        public Builder setExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder setBank(Bank bank) {
            this.bank = bank;
            return this;
        }

        public Builder setTreyPassword(int  tryPassword) {
            this.tryPassword = tryPassword;
            return this;
        }

        public Wallet build(){
            return new Wallet(tryPassword,id,cvv2,expirationDate,password,customer,bank);
        }
    }
}
