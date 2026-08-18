package com.example.moneytransfer.config;

import com.example.moneytransfer.model.Account;
import com.example.moneytransfer.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(AccountRepository repository) {
        return args -> {
            repository.save(new Account("acc-1", "Alice", new BigDecimal("1000.00")));
            repository.save(new Account("acc-2", "Bob", new BigDecimal("500.00")));
            repository.save(new Account("acc-3", "Carol", new BigDecimal("250.00")));
        };
    }
}
