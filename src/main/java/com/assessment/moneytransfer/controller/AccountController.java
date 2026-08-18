package com.example.moneytransfer.controller;

import com.example.moneytransfer.dto.AccountRequest;
import com.example.moneytransfer.model.Account;
import com.example.moneytransfer.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping
    public Account createAccount(@RequestBody AccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable String id) {
        return accountService.getAccount(id);
    }
}
