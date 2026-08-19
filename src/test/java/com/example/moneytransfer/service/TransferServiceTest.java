package com.example.moneytransfer.service;

import com.example.moneytransfer.dto.TransferRequest;
import com.example.moneytransfer.exception.AccountNotFoundException;
import com.example.moneytransfer.exception.InsufficientFundsException;
import com.example.moneytransfer.exception.InvalidTransferException;
import com.example.moneytransfer.model.Account;
import com.example.moneytransfer.repository.AccountRepository;
import com.example.moneytransfer.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransferServiceTest {

    private AccountRepository repository;
    private TransferRepository transferRepository;
    private TransferService service;

    @BeforeEach
    void setUp() {
        repository = new AccountRepository();
        transferRepository = new TransferRepository();
        repository.save(new Account("acc-1", "Alice", new BigDecimal("1000.00")));
        repository.save(new Account("acc-2", "Bob", new BigDecimal("500.00")));
        service = new TransferService(repository, transferRepository);
    }

    private TransferRequest request(String from, String to, String amount) {
        TransferRequest r = new TransferRequest();
        r.setFromAccountId(from);
        r.setToAccountId(to);
        r.setAmount(amount == null ? null : new BigDecimal(amount));
        return r;
    }

    @Test
    void shouldTransferSuccessfully() {
        service.transfer(request("acc-1", "acc-2", "100.00"));

        assertEquals(new BigDecimal("900.00"), repository.findById("acc-1").get().getBalance());
        assertEquals(new BigDecimal("600.00"), repository.findById("acc-2").get().getBalance());
    }

    @Test
    void shouldThrowWhenInsufficientFunds() {
        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(request("acc-1", "acc-2", "5000.00")));

        assertEquals(new BigDecimal("1000.00"), repository.findById("acc-1").get().getBalance());
        assertEquals(new BigDecimal("500.00"), repository.findById("acc-2").get().getBalance());
    }

    @Test
    void shouldThrowWhenSourceAccountNotFound() {
        assertThrows(AccountNotFoundException.class,
                () -> service.transfer(request("does-not-exist", "acc-2", "10.00")));
    }

    @Test
    void shouldThrowWhenDestinationAccountNotFound() {
        assertThrows(AccountNotFoundException.class,
                () -> service.transfer(request("acc-1", "does-not-exist", "10.00")));
    }

    @Test
    void shouldThrowWhenTransferringToSameAccount() {
        assertThrows(InvalidTransferException.class,
                () -> service.transfer(request("acc-1", "acc-1", "10.00")));
    }

    @Test
    void shouldThrowWhenAmountIsZero() {
        assertThrows(InvalidTransferException.class,
                () -> service.transfer(request("acc-1", "acc-2", "0")));
    }

    @Test
    void shouldThrowWhenAmountIsNegative() {
        assertThrows(InvalidTransferException.class,
                () -> service.transfer(request("acc-1", "acc-2", "-10.00")));
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        assertThrows(InvalidTransferException.class,
                () -> service.transfer(request("acc-1", "acc-2", null)));
    }

    @Test
    void shouldThrowWhenTransferRequestIsNull() {
        assertThrows(InvalidTransferException.class,
                () -> service.transfer(null));
    }

    @Test
    void concurrentTransfersShouldNotCorruptBalances() throws InterruptedException {
        int threads = 50;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    service.transfer(request("acc-1", "acc-2", "1.00"));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        pool.shutdown();

        assertEquals(new BigDecimal("950.00"), repository.findById("acc-1").get().getBalance());
        assertEquals(new BigDecimal("550.00"), repository.findById("acc-2").get().getBalance());
    }
}
