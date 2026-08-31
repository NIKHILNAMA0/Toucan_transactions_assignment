AI Usage Disclosure

1. Tools Used

I used ChatGPT as an AI coding assistant while completing this assignment.

2. What I Used AI For

- I used ChatGPT to understand the starter Spring Boot project, learn the roles of controllers, services, repositories, DTOs and to get guidance while implementing the required REST APIs.
- I also used it to help diagnose compilation errors, understand Maven test failures, design automated tests, and review the validation and error-handling approach.

3. Significant AI-Generated Suggestions

AI suggested the layered structure:

Controller → Service → Repository → Database

It also suggested using DTOs for request validation, custom exceptions for
transaction-not-found and duplicate-transaction cases, and a global exception
handler.

AI also suggested the transaction status lifecycle:

PENDING → COMPLETED
PENDING → FAILED
PENDING → CANCELLED

with the terminal states preventing further status changes.


4. What I Changed or Corrected

I reviewed and tested the generated suggestions while implementing them.

One significant issue occurred when the test class was initially placed under src/main/java instead of src/test/java. This caused compilation errors because JUnit and Spring test classes are test dependencies. I identified the issue from the Maven error and moved TransactionControllerTests.java to the correct src/test/java directory.

There were also compilation issues involving the TransactionStatus and TransactionType enum files and the TransactionRepository methods. These were corrected during implementation and verified by rebuilding the project.

I also removed unused imports from the test class after checking which classes were actually referenced.

5. AI Mistakes or Issues That Had to Be Fixed

The main issue caused by the AI-generated implementation was the incorrect location of the test class. The generated test code itself was valid, but placing it in the main source directory caused Maven to compile it as application code.

The repository and enum compilation issues were also diagnosed and corrected during development rather than being accepted without verification.

6. How I Verified the Final Result

I verified the application by running the Spring Boot application and manually testing all four REST operations.

I tested:
- Successful transaction creation
- Transaction retrieval
- Transaction status update
- Retrieval of customer transactions
- Validation failure
- Duplicate Transaction ID handling
- Invalid status transition

I also created automated tests using JUnit and MockMvc.

The final project was verified using:

./mvnw clean test

The final Maven build completed successfully with all automated tests passing.

I reviewed the final implementation and verified that the submitted code matches the documented API, validation rules, business rules, and testing approach.
