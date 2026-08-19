package com.example.moneytransfer.dto;

import java.math.BigDecimal;

public class AccountRequest {

    private String id;
    private String ownerName;
    private BigDecimal balance;

    public AccountRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}