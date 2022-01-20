package com.maktab.bank.repository;

import com.maktab.bank.customexception.ManyException;
import com.maktab.bank.customexception.NotFoundTransactionException;
import com.maktab.bank.customexception.PasswordException;
import com.maktab.bank.customexception.TryPasswordException;
import com.maktab.bank.model.Customer;
import com.maktab.bank.model.Transaction;
import com.maktab.bank.model.Wallet;
import com.maktab.bank.model.enumType.TransactionType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public static final int KARMOZD=600;
    private WalletRepository walletRepository = new WalletRepository();

    public void save(Connection connection,Transaction transaction) throws SQLException {
        String insert="insert into bank.transaction(date, price, type, customer_id, wallet_id) values \n" +
                "(?,?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setDate(1,transaction.getDate());
        preparedStatement.setInt(2,transaction.getPrice());
        preparedStatement.setString(3,transaction.getType().toString());
        preparedStatement.setLong(4,transaction.getCustomer().getId());
        preparedStatement.setLong(5,transaction.getWallet().getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

//    public void remove(Connection connection,long id) throws SQLException {
//        String sql="delete\n" +
//        "from bank.transaction\n" +
//        "where bank.transaction.id=?;";
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        preparedStatement.setLong(1,id);
//        preparedStatement.executeUpdate();
//    }

    public Transaction find(Connection connection,long id) throws SQLException {
        String sql="\n" +
                "select *\n" +
                "from bank.transaction\n" +
                "where bank.transaction.id =?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return createTransaction(resultSet);
        }
        throw new NotFoundTransactionException("not found transaction");
    }

//    public void update(Connection connection,Transaction transaction) throws SQLException {
//        String sql="\n" +
//                "update bank.transaction t set \n" +
//                "t.date=? ,t.price=?,t.type=?,t.customer_id=?,t.wallet_id=? where t.id=?;";
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        preparedStatement.setDate(1,transaction.getDate());
//        preparedStatement.setInt(2,transaction.getPrice());
//        preparedStatement.setString(3,transaction.getType().toString());
//        preparedStatement.setLong(4,transaction.getCustomer().getId());
//        preparedStatement.setLong(5,transaction.getWallet().getId());
//        preparedStatement.setLong(6,transaction.getId());
//        preparedStatement.executeUpdate();
//        preparedStatement.close();
//    }

    public List<Transaction> findBuUserId(Connection connection,long userId,long walletId) throws SQLException {
        String sql="select *\n" +
                "from bank.transaction\n" +
                "where bank.transaction.customer_id =? and wallet_id =?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,userId);
        preparedStatement.setLong(2,walletId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Transaction> transactions=new ArrayList<>();
        while (resultSet.next()){
            transactions.add(createTransaction(resultSet));
        }
        return transactions;
    }



    public void cardToCard(Connection connection,long id,String password,long idTo,int price) throws SQLException {
        Wallet myWallet = walletRepository.find(connection, id,password);
        if(myWallet.getTryPassword()>=3)
            throw new TryPasswordException("try password is complite");
        if(!myWallet.getPassword().equals(password)) {
            myWallet.setTryPassword(myWallet.getTryPassword()+1);
            walletRepository.updateTryPassword(connection,myWallet);
            throw new PasswordException("password exception");
        }
        Wallet otherWallet = walletRepository.findDeposit(connection, idTo);
        if(getDeposit(connection,id)-getWithdraw(connection,id)-KARMOZD<0)
            throw new ManyException("not enugh many");
        Transaction myTransaction = createTransactionResponse(price+KARMOZD,myWallet,TransactionType.WITHDRAW);
        Transaction otherTransaction = createTransactionResponse(price,otherWallet,TransactionType.DEPOSIT);
        save(connection,myTransaction);
        save(connection,otherTransaction);
    }

    public int getDeposit(Connection connection,long walletId) throws SQLException {
        String sql="select sum(price) as price \n" +
                "from bank.transaction where wallet_id=? and type=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,walletId);
        preparedStatement.setString(2,TransactionType.DEPOSIT.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("price");
    }

    public int getWithdraw(Connection connection,long walletId) throws SQLException {
        String sql="select sum(price) as price \n" +
                "from bank.transaction where wallet_id=? and type=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,walletId);
        preparedStatement.setString(2,TransactionType.WITHDRAW.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("price");
    }


    private Transaction createTransactionResponse(int price, Wallet myWallet, TransactionType withdraw) {
        return Transaction.builder()
                .setDate(Date.valueOf(LocalDate.now()))
                .setPrice(price)
                .setType(withdraw)
                .setCustomer(myWallet.getCustomer())
                .setWallet(myWallet)
                .build();
    }

    private Transaction createTransaction(ResultSet resultSet) throws SQLException {
        return Transaction.builder()
                .setId(resultSet.getLong("id"))
                .setDate(resultSet.getDate("date"))
                .setPrice(resultSet.getInt("price"))
                .setType(TransactionType.valueOf(resultSet.getString("type")))
                .setCustomer(new Customer(resultSet.getLong("customer_id")))
                .setWallet(new Wallet(resultSet.getLong("wallet_id")))
                .build();
    }

}
