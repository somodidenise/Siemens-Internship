# Siemens Java Internship - Code Refactoring Project

This repository contains a refactored Spring Boot application that implements a simple CRUD system with asynchronous processing. The original version had functional coverage but lacked structure, documentation, and error handling.

---

## What Was Refactored

### 1.  Logical Issues Fixed
- Replaced incorrect HTTP status codes 
- Corrected asynchronous processing logic

### 2.  Error Handling and Validation
- Implemented centralized error handling using `@RestControllerAdvice`
- Added field-level validation using `@NotBlank`, `@Email`, and `@Pattern`

### 3.  Asynchronous `processItemsAsync`
- Fully refactored with `CompletableFuture`
- Thread-safe using `CopyOnWriteArrayList` and `AtomicInteger`
- Ensures all items are processed before returning

### 4.  Tests
- Unit tests using JUnit 5 and Mockito
- Controller tests using Spring’s `MockMvc`
- Validation logic tested (including invalid email case)

---

##  How to Run the App

```bash
./mvnw spring-boot:run
