package com.example.moneytransfer.model;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final String id;
    private final String ownerName;
    private BigDecimal balance;
    private final ReentrantLock lock = new ReentrantLock();

    public Account(String id, String ownerName, BigDecimal balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}
