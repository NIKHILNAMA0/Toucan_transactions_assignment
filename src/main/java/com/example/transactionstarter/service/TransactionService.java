package com.example.transactionstarter.service;

import com.example.transactionstarter.entity.TransactionStatus;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.UpdateStatusRequest;
import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import com.example.transactionstarter.exceptions.DuplicateTransactionException;
import com.example.transactionstarter.exceptions.TransactionNotFoundException;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(CreateTransactionRequest request) {

        if (transactionRepository.existsById(request.getTransactionId())) {
            throw new DuplicateTransactionException("Transaction already exists: " + request.getTransactionId());
        }

        if (request.getTransactionStatus() != TransactionStatus.PENDING) {
    throw new IllegalArgumentException("New transactions must have PENDING status");
}

Transaction transaction = new Transaction(
        request.getTransactionId(),
        request.getCustomerId(),
        request.getAmount(),
        request.getCurrency().toUpperCase(),
        request.getTransactionType(),
        request.getTransactionStatus()
);

        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(String transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new TransactionNotFoundException("Transaction not found: " + transactionId));
    }

    public Transaction updateStatus(String transactionId,UpdateStatusRequest request) {

    Transaction transaction = getTransaction(transactionId);

    TransactionStatus currentStatus =
            transaction.getTransactionStatus();

    TransactionStatus newStatus = request.getStatus();

    if (currentStatus != TransactionStatus.PENDING) {throw new IllegalArgumentException("Transaction status cannot be changed from "+ currentStatus);
    }

    transaction.setTransactionStatus(newStatus);

    return transactionRepository.save(transaction);
}

    public List<Transaction> getCustomerTransactions(String customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }
}