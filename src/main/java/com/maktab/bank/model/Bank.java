package com.maktab.bank.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bank {

    private long id;
    private String name;

    public Bank(long id) {
        this.id = id;
    }

    public Bank(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Bank(Connection connection) throws SQLException {
        String query="CREATE TABLE IF NOT EXISTS bank.bank(\n" +
                "    id serial primary key ,\n" +
                "    name varchar(200)\n" +
                ");insert into bank.bank(name) values ('meli'),('sepah');";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder{

        private long id;
        private String name;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Bank build(){
            return new Bank(id,name);
        }
    }
}
