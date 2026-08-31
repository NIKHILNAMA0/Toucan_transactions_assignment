# Toucan Payments - Transaction Processing Service

# 1. Understanding of the Problem

This project implements a small transaction-processing REST service using Java, Spring Boot, Spring Data JPA, and an H2 embedded database.



The application implements the four required operations:
1. Create a transaction
2. Get a transaction by Transaction ID
3. Update the status of an existing transaction
4. Get all transactions for a Customer ID

Each transaction contains:
- Transaction ID
- Customer ID
- Amount
- Currency
- Transaction Type
- Transaction Status


# 2. Assumptions

The following assumptions were made:

- Transaction IDs are unique.
- Transaction IDs and Customer IDs cannot be blank.
- Amount must be greater than zero.
- Currency must be a three-character currency code.
- Transaction Type and Transaction Status are required.
- A newly created transaction must start with PENDING status.
- A transaction must exist before its status can be updated.
- COMPLETED, FAILED, and CANCELLED are treated as terminal statuses.
- A transaction in a terminal status cannot be changed to another status.

# 3. Validation Rules

The following validation rules are applied when creating a transaction:

- Transaction ID is required and cannot be blank.
- Customer ID is required and cannot be blank.
- Amount is required and must be greater than 0.
- Currency is required and must contain exactly 3 characters.
- Transaction type is required.
- Initial transaction status is required.

Additional business validation:

- Duplicate Transaction IDs are rejected with HTTP 409 Conflict.
- New transactions must start with PENDING status.
- A transaction must exist before its status can be updated.
- A transaction cannot be changed after reaching a terminal status.
- Invalid request data returns HTTP 400 Bad Request.
- A missing transaction returns HTTP 404 Not Found.

# 4. API Endpoints

# Create Transaction

POST /api/transactions

Example request:

{
  "transactionId": "TXN001",
  "customerId": "CUST001",
  "amount": 1000.00,
  "currency": "INR",
  "transactionType": "PAYMENT",
  "transactionStatus": "PENDING"
}

Successful creation returns HTTP 201 Created.

# Get Transaction

GET /api/transactions/{transactionId}

Example:

GET /api/transactions/TXN001


# Update Transaction Status

PATCH /api/transactions/{transactionId}/status

Example request:

{
  "status": "COMPLETED"
}


# Get Customer Transactions

GET /api/customers/{customerId}/transactions

Example:

GET /api/customers/CUST001/transactions


# 5. Error Handling

A global exception handler provides consistent HTTP responses:

- HTTP 400 Bad Request for validation and business-validation failures
- HTTP 404 Not Found for missing transactions
- HTTP 409 Conflict for duplicate Transaction IDs

Example:

{
  "error": "Transaction not found: TXN999"
}


# 6. Testing Approach

Automated API tests are implemented using JUnit and Spring Boot MockMvc.

The test suite covers:
- Successful transaction creation
- Transaction retrieval
- Transaction status update
- Retrieving customer transactions
- Validation failure
- Duplicate Transaction ID
- Invalid status transition

The project is tested using:

./mvnw clean test


# 7. Known Limitations

- H2 is used as the database because it is provided by the starter project.
- Authentication and authorization are not implemented.
- Error responses are simple.
- The transaction model contains only the fields required for this exercise.
- No external payment provider integration is implemented.


# 8. What I Would Improve With More Time

- Add authentication and authorization.
- Add production database configuration.
- Introduce a dedicated error-response DTO.
- Add more unit tests for service-layer business rules.

