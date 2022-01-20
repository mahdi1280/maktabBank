package com.maktab.bank.repository;

import com.maktab.bank.model.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankRepository {

    public List<Bank> getBankList(Connection connection) throws SQLException {
        String sql = "select *\n" +
                "from bank.bank;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Bank> banks = new ArrayList<>();
        while (resultSet.next()) {
            banks.add(createBank(resultSet));
        }
        return banks;
    }

    private Bank createBank(ResultSet resultSet) throws SQLException {
        return Bank.builder()
                .setId(resultSet.getLong("id"))
                .setName(resultSet.getString("name"))
                .build();
    }
}
