package com.example.moneytransfer.service;

import com.example.moneytransfer.dto.TransferRequest;
import com.example.moneytransfer.exception.AccountNotFoundException;
import com.example.moneytransfer.exception.InsufficientFundsException;
import com.example.moneytransfer.exception.InvalidTransferException;
import com.example.moneytransfer.model.Account;
import com.example.moneytransfer.model.Transfer;
import com.example.moneytransfer.repository.AccountRepository;
import com.example.moneytransfer.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferService(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    public void transfer(TransferRequest request) {
        validate(request);

        Account from = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found: " + request.getFromAccountId()));
        Account to = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found: " + request.getToAccountId()));

        Account first = from.getId().compareTo(to.getId()) < 0 ? from : to;
        Account second = first == from ? to : from;

        first.getLock().lock();
        try {
            second.getLock().lock();
            try {
                if (from.getBalance().compareTo(request.getAmount()) < 0) {
                    throw new InsufficientFundsException(
                            "Insufficient funds in account: " + from.getId());
                }
                from.setBalance(from.getBalance().subtract(request.getAmount()));
                to.setBalance(to.getBalance().add(request.getAmount()));
                transferRepository.save(new Transfer(
                    from.getId(), to.getId(), request.getAmount()));
            } finally {
                second.getLock().unlock();
            }
        } finally {
            first.getLock().unlock();
        }
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    private void validate(TransferRequest request) {
        if (request == null) {
            throw new InvalidTransferException("Transfer request is required");
        }
        if (request.getFromAccountId() == null || request.getToAccountId() == null) {
            throw new InvalidTransferException("fromAccountId and toAccountId are required");
        }
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new InvalidTransferException("Cannot transfer to the same account");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Amount must be greater than zero");
        }
    }
}
