package com.example.transactionstarter.dto;

import com.example.transactionstarter.entity.TransactionStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {

    @NotNull
    private TransactionStatus status;

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}