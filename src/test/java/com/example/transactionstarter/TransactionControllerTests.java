package com.example.transactionstarter;


import com.example.transactionstarter.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void cleanDatabase() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldCreateTransaction() throws Exception {

        String request = """
                {
                    "transactionId": "TXN001",
                    "customerId": "CUST001",
                    "amount": 1000.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "transactionStatus": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TXN001"))
                .andExpect(jsonPath("$.customerId").value("CUST001"))
                .andExpect(jsonPath("$.amount").value(1000.00));
    }

    @Test
    void shouldGetTransaction() throws Exception {

        createTransaction();

        mockMvc.perform(get("/api/transactions/TXN001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TXN001"))
                .andExpect(jsonPath("$.customerId").value("CUST001"));
    }

    @Test
    void shouldUpdateTransactionStatus() throws Exception {

        createTransaction();

        String request = """
                {
                    "status": "COMPLETED"
                }
                """;

        mockMvc.perform(patch("/api/transactions/TXN001/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionStatus").value("COMPLETED"));
    }

    @Test
    void shouldGetCustomerTransactions() throws Exception {

        createTransaction();

        String secondTransaction = """
                {
                    "transactionId": "TXN002",
                    "customerId": "CUST001",
                    "amount": 500.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "transactionStatus": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondTransaction))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/customers/CUST001/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
void shouldRejectInvalidAmount() throws Exception {

    String request = """
            {
                "transactionId": "TXN003",
                "customerId": "CUST001",
                "amount": -100.00,
                "currency": "INR",
                "transactionType": "PAYMENT",
                "transactionStatus": "PENDING"
            }
            """;

    mockMvc.perform(post("/api/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.amount").exists());
}

@Test
void shouldRejectDuplicateTransaction() throws Exception {

    createTransaction();

    String duplicateRequest = """
            {
                "transactionId": "TXN001",
                "customerId": "CUST001",
                "amount": 500.00,
                "currency": "INR",
                "transactionType": "PAYMENT",
                "transactionStatus": "PENDING"
            }
            """;

    mockMvc.perform(post("/api/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(duplicateRequest))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error")
                    .value("Transaction already exists: TXN001"));
}

@Test
void shouldRejectStatusChangeFromCompletedTransaction() throws Exception {

    createTransaction();

    String completeRequest = """
            {
                "status": "COMPLETED"
            }
            """;

    mockMvc.perform(patch("/api/transactions/TXN001/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(completeRequest))
            .andExpect(status().isOk());

    String secondUpdateRequest = """
            {
                "status": "FAILED"
            }
            """;

    mockMvc.perform(patch("/api/transactions/TXN001/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(secondUpdateRequest))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error")
                    .value("Transaction status cannot be changed from COMPLETED"));
}

    private void createTransaction() throws Exception {

        String request = """
                {
                    "transactionId": "TXN001",
                    "customerId": "CUST001",
                    "amount": 1000.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "transactionStatus": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated());
    }
}