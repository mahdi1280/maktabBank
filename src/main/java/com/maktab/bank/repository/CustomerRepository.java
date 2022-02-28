package com.maktab.bank.repository;

import com.maktab.bank.customexception.CustomerNotFoundException;
import com.maktab.bank.model.Customer;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomerRepository {

    public Customer save(Customer customer) {
        Session session=MySession.getSession();
        session.beginTransaction();
        try {
            Long id= (Long) MySession.getSession().save(customer);
            session.getTransaction().commit();
            return session.get(Customer.class,id);
        }catch (Exception e){
            session.getTransaction().rollback();
        }
        return null;
    }

    public Customer find(long customerId) {
        Customer customer = MySession.getSession().find(Customer.class, customerId);
        if(customer==null)
            throw new CustomerNotFoundException("customer not found");
        return customer;
    }

    public Customer login(Customer customer) throws SQLException {
        Session session=MySession.getSession();
        List<Customer> resultList = session.createQuery("select c from Customer c where c.password=:password and c.userName=:username")
                .setParameter("password", customer.getPassword())
                .setParameter("username", customer.getUserName())
                .list();
        if (resultList==null || resultList.isEmpty())
            throw new CustomerNotFoundException("customer not found exception");
        return resultList.get(0);
    }
}
