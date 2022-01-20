package com.maktab.bank.repository;

import com.maktab.bank.customexception.CustomerNotFoundException;
import com.maktab.bank.model.Customer;

import java.sql.*;

public class CustomerRepository {

    public Customer save(Connection connection, Customer customer) throws SQLException {
        String sql="insert into bank.customer(user_name, password, created_at) values \n" +
                "(?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1,customer.getUserName());
        preparedStatement.setString(2,customer.getPassword());
        preparedStatement.setDate(3,customer.getCreatedAt());
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        long customerId=resultSet.getLong(1);
        preparedStatement.close();
        return find(connection,customerId);
    }

    public Customer find(Connection connection,long customerId) throws SQLException {
        String sql="select *\n" +
                "from bank.customer where id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,customerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next())
            throw new CustomerNotFoundException("customer not found");
        return createCustomer(resultSet);
    }

    private Customer createCustomer(ResultSet resultSet) throws SQLException {
        return Customer.builder()
                .setId(resultSet.getLong("id"))
                .setUserName(resultSet.getString("user_name"))
                .setPassword(resultSet.getString("password"))
                .setCreatedAt(resultSet.getDate("created_at"))
                .build();
    }

    public Customer login(Connection connection, Customer customer) throws SQLException {

        String sql="\n" +
                "select *\n" +
                "from bank.customer where user_name=? and password=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,customer.getUserName());
        preparedStatement.setString(2,customer.getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next())
            throw new CustomerNotFoundException("customer not found exception");
        return createCustomer(resultSet);
    }
}
