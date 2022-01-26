package com.maktab.bank.repository;

import com.maktab.bank.customexception.NotFoundWalletException;
import com.maktab.bank.customexception.PasswordException;
import com.maktab.bank.model.Bank;
import com.maktab.bank.model.Customer;
import com.maktab.bank.model.Wallet;

import java.sql.*;

public class WalletRepository {

    public void save(Connection connection, Wallet wallet) throws SQLException {
        String sql="\n" +
                "insert into bank.wallet(id, cvv2, expirationdate, password, customer_id, bank_id) values \n" +
                "(nextval('bank.wallet_sec'),?,?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1,wallet.getCvv2());
        preparedStatement.setDate(2,wallet.getExpirationDate());
        preparedStatement.setString(3,wallet.getPassword());
        preparedStatement.setLong(4,wallet.getCustomer().getId());
        preparedStatement.setLong(5,wallet.getBank().getId());
        preparedStatement.executeUpdate();
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    public void update(Connection connection,Wallet wallet) throws SQLException {
        String sql="update bank.wallet\n" +
                "set\n" +
                "     cvv2=?, expirationdate=?, password=?, customer_id=?, bank_id=?\n" +
                "where id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,wallet.getCvv2());
        preparedStatement.setDate(2,wallet.getExpirationDate());
        preparedStatement.setString(3,wallet.getPassword());
        preparedStatement.setLong(4,wallet.getCustomer().getId());
        preparedStatement.setLong(5,wallet.getBank().getId());
        preparedStatement.setLong(6,wallet.getId());
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void remove(Connection connection,long id,String password) throws SQLException {
        find(connection,id,password);
        String sql="delete FROM bank.wallet where id=?;\n";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,id);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public Wallet find(Connection connection,long id,String password) throws SQLException {
        String sql="select *\n" +
                "from bank.wallet\n" +
                "where id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            Wallet build = Wallet.builder()
                    .setId(resultSet.getLong("id"))
                    .setCvv2(resultSet.getString("cvv2"))
                    .setExpirationDate(resultSet.getDate("expirationdate"))
                    .setPassword(resultSet.getString("password"))
                    .setCustomer(new Customer(resultSet.getLong("customer_id")))
                    .setBank(new Bank(resultSet.getLong("bank_id")))
                    .setTreyPassword(resultSet.getInt("try_password"))
                    .build();
            if(!resultSet.getString("password").equals(password)) {
                updateTryPassword(connection,build);
                throw new PasswordException("password not valid");
            }
            if(resultSet.getInt("try_password")>3){
                System.out.println("shoma bish az andaze password eshtebah vared karde id.");
                throw new PasswordException("password not valid");
            }
            return build;
        }
        throw new NotFoundWalletException("wallet not found");
    }

    public Wallet findDeposit(Connection connection,long id) throws SQLException {
        String sql="select *\n" +
                "from bank.wallet\n" +
                "where id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
           return Wallet.builder()
                    .setId(resultSet.getLong("id"))
                    .setCvv2(resultSet.getString("cvv2"))
                    .setExpirationDate(resultSet.getDate("expirationdate"))
                    .setPassword(resultSet.getString("password"))
                    .setCustomer(new Customer(resultSet.getLong("customer_id")))
                    .setBank(new Bank(resultSet.getLong("bank_id")))
                    .setTreyPassword(resultSet.getInt("try_password"))
                    .build();
        }
        throw new NotFoundWalletException("wallet not found");
    }

    public void changePassword(Connection connection,long walletId,String password,String newPassword) throws SQLException {
        Wallet wallet = find(connection, walletId , password);
        wallet.setPassword(newPassword);
        update(connection,wallet);
    }

    public void updateTryPassword(Connection connection, Wallet myWallet) throws SQLException {
        find(connection,myWallet.getId(),myWallet.getPassword());
        String sql="update bank.wallet\n" +
                "set\n" +
                "     try_password=? " +
                "where id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,myWallet.getTryPassword()+1);
        preparedStatement.setLong(2,myWallet.getId());
        preparedStatement.execute();
        preparedStatement.close();
    }
}
