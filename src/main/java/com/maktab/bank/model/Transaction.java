package com.maktab.bank.model;

import com.maktab.bank.model.enumType.TransactionType;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaction {

   private long id;
   private Date date;
   private int price;
   private TransactionType type;

   private Customer customer;
   private Wallet wallet;

    public Transaction(long id, Date date, int price, TransactionType type, Customer customer, Wallet wallet) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.type = type;
        this.customer = customer;
        this.wallet = wallet;
    }

    public Transaction(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS bank.transaction(\n" +
                "    id serial primary key ,\n" +
                "    date date,\n" +
                "    price int,\n" +
                "    type varchar(200),\n" +
                "    customer_id int,\n" +
                "    wallet_id bigint,\n" +
                "    CONSTRAINT fk_customerr_id FOREIGN KEY (customer_id)\n" +
                "        REFERENCES bank.customer (id),\n" +
                "    CONSTRAINT fk_wallett_id FOREIGN KEY (wallet_id)\n" +
                "        REFERENCES bank.wallet (id)\n" +
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", price=" + price +
                ", type=" + type +
                ", customer=" + customer +
                ", wallet=" + wallet +
                '}';
    }

    public static class Builder{

        private long id;
        private Date date;
        private int price;
        private TransactionType type;

        private Customer customer;
        private Wallet wallet;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder setPrice(int price) {
            this.price = price;
            return this;
        }

        public Builder setType(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder setWallet(Wallet wallet) {
            this.wallet = wallet;
            return this;
        }

        public Transaction build(){
            return new Transaction(id,date,price,type,customer,wallet);
        }
    }
}
