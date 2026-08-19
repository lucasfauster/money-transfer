package com.example.moneytransfer.service;

import com.example.moneytransfer.dto.AccountRequest;
import com.example.moneytransfer.exception.AccountNotFoundException;
import com.example.moneytransfer.model.Account;
import com.example.moneytransfer.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account createAccount(AccountRequest request) {
        return accountRepository.save(new Account(request.getId(), request.getOwnerName(), request.getBalance()));
    }

    public Account getAccount(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + id));
    }
}