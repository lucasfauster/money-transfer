package com.example.moneytransfer.repository;

import com.example.moneytransfer.model.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public Optional<Account> findById(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public Account save(Account account) {
        accounts.put(account.getId(), account);
        return account;
    }

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }
}
