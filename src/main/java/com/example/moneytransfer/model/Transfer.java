package com.example.moneytransfer.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Transfer {

    private final String id;
    private final String fromAccountId;
    private final String toAccountId;
    private final BigDecimal amount;

    public Transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        this(UUID.randomUUID().toString(), fromAccountId, toAccountId, amount);
    }

    public Transfer(String id, String fromAccountId, String toAccountId, BigDecimal amount) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}