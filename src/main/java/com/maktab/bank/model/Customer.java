package com.maktab.bank.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Customer {

    private long id;
    private String userName;
    private String password;
    private Date createdAt;

    public Customer(long id) {
        this.id = id;
    }

    public Customer(long id, String userName, String password, Date createdAt) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.createdAt = createdAt;
    }

    public Customer(Connection connection) throws SQLException {
        String dbSql="create schema if not exists bank;";
        String sql="CREATE TABLE IF NOT EXISTS bank.customer(\n" +
                "    id SERIAL PRIMARY KEY,\n" +
                "    user_name VARCHAR(200),\n" +
                "    password VARCHAR(200),\n" +
                "    created_at DATE\n" +
                ");";
        PreparedStatement preparedStatement1 = connection.prepareStatement(dbSql);
        preparedStatement1.executeUpdate();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
        preparedStatement1.close();
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static class Builder{

        private long id;
        private String userName;
        private String password;
        private Date createdAt;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Customer build(){
            return new Customer(id,userName,password,createdAt);
        }
    }
}
