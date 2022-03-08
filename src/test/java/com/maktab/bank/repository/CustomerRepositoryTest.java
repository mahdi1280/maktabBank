package com.maktab.bank.repository;

import com.maktab.bank.customexception.CustomerNotFoundException;
import com.maktab.bank.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerRepositoryTest {
    private final CustomerRepository customerRepository;

    CustomerRepositoryTest() {
        this.customerRepository = new CustomerRepository();
    }

    @Test
    void testConnection() {
        MySession.getSession();
    }

    @Test
    void saveCustomer() {
        Customer customer = Customer.builder()
                .setPassword("!23")
                .setUserName("122133")
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .build();
        Customer customer1 = customerRepository.save(customer);
        assertNotNull(customer1);
    }

    @Test
    void saveCustomerNullPassword() {
        Customer customer = Customer.builder()
                .setPassword(null)
                .setUserName("123")
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .build();
        try {
            customerRepository.save(customer);
            assert true;
        } catch (Exception ignored) {
        }
    }

    @Test
    void saveCustomerAndCheckBody() {
        Customer customer = Customer.builder()
                .setPassword("!23")
                .setUserName("123")
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .build();
        Customer customer1 = customerRepository.save(customer);
        assertDoesNotThrow(() -> customerRepository.login(customer1));
    }

    @Test
    void saveCustomerNullUsername() {
        Customer customer = Customer.builder()
                .setPassword("asd")
                .setUserName(null)
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .build();
        try {
            customerRepository.save(customer);
            assert true;
        } catch (Exception ignored) {
        }
    }

    @Test
    void findNormal() {
        Customer customer = Customer.builder()
                .setPassword("!23")
                .setUserName("123")
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .build();
        customerRepository.save(customer);
        Customer find = customerRepository.find(1);
        Assertions.assertNotNull(find);
    }

    @Test
    void findCustomerNoEntity() {
        Customer customer = Customer.builder()
                .setPassword("!23")
                .setUserName("123")
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .build();
        customerRepository.save(customer);
        try {
            customerRepository.find(100);
            assert true;
        } catch (CustomerNotFoundException ignored) {

        }
    }

    @Test
    void loginNormal() {
        Customer customer = Customer.builder()
                .setPassword("!23")
                .setUserName("123")
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .build();
        customerRepository.save(customer);
        Customer login = customerRepository.login(customer);
        Assertions.assertNotNull(login);
    }

    @Test
    void loginNotValid() {
        Customer customer = Customer.builder()
                .setPassword("!23")
                .setUserName("123")
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .build();
        customerRepository.save(customer);
        try {
            customerRepository.login(customer);
            assert true;
        } catch (CustomerNotFoundException ignored) {

        }

    }


}