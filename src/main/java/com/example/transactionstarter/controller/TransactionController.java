package com.example.transactionstarter.controller;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.UpdateStatusRequest;
import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        Transaction created =
                transactionService.createTransaction(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(
            @PathVariable String transactionId) {

        Transaction transaction =
                transactionService.getTransaction(transactionId);

        return ResponseEntity.ok(transaction);
    }

    @PatchMapping("/transactions/{transactionId}/status")
    public ResponseEntity<Transaction> updateStatus(
            @PathVariable String transactionId,
            @Valid @RequestBody UpdateStatusRequest request) {

        Transaction updated =
                transactionService.updateStatus(transactionId, request);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/customers/{customerId}/transactions")
    public ResponseEntity<List<Transaction>> getCustomerTransactions(
            @PathVariable String customerId) {

        List<Transaction> transactions =
                transactionService.getCustomerTransactions(customerId);

        return ResponseEntity.ok(transactions);
    }
}