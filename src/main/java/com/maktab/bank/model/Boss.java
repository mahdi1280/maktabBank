package com.maktab.bank.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Boss {

    private long id;
    private String lastName;
    private String firstName;

    public Boss(Connection connection) throws SQLException {
        String sql = "create table if not exists bank.boss(\n" +
                "    id serial primary key ,\n" +
                "    lsat_name varchar(200),\n" +
                "    first_name varchar(200),\n" +
                "    bank_id int unique,\n" +
                "    CONSTRAINT fk_bank_id FOREIGN KEY (bank_id)\n" +
                "        REFERENCES bank.bank (id)\n" +
                ");";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
