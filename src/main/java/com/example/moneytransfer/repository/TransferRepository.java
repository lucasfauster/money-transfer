package com.example.moneytransfer.repository;

import com.example.moneytransfer.model.Transfer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransferRepository {

    private final Map<String, Transfer> transfers = new ConcurrentHashMap<>();

    public Optional<Transfer> findById(String id) {
        return Optional.ofNullable(transfers.get(id));
    }

    public Transfer save(Transfer transfer) {
        transfers.put(transfer.getId(), transfer);
        return transfer;
    }

    public List<Transfer> findAll() {
        return new ArrayList<>(transfers.values());
    }
}