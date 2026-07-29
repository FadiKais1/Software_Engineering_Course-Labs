# Software Engineering – Lab 9
## JUnit & TestFX Unit Testing

### Authors
- Fadi Kais (324222512)
- Yousef Salem (213537459)

---

## Overview

This branch extends the Lab 2 JavaFX Login System by adding automated unit and GUI tests using **JUnit 5** and **TestFX**, as required in Lab 9.

The goal of this lab is to demonstrate automated testing of a JavaFX application, verify the correctness of the user interface, and ensure that core login functionality behaves as expected.

---

## Technologies

- Java 17
- JavaFX 21
- Maven
- JUnit 5
- TestFX
- Hamcrest

---

## Implemented Tests

The following TestFX tests were implemented:

### 1. Username Field Initialization
Verifies that the username text field is empty when the application starts.

### 2. Password Field Initialization
Verifies that the password field is empty when the application starts.

### 3. Login Button Verification
Checks that the login button exists and displays the expected text ("Login").

### 4. Invalid Login Test
Simulates entering invalid credentials, presses the Login button, and verifies that the correct error message is displayed.

---

## Running the Tests

Execute:

```bash
mvn clean test
```

Expected result:

```
Tests run: 4
Failures: 0
Errors: 0
BUILD SUCCESS
```

---

## Project Structure

```
src
├── main
│   ├── java
│   └── resources
└── test
    └── java
        └── AppTest.java
```

---

## Notes

- Testing was implemented using TestFX together with JUnit 5.
- Maven Surefire Plugin is used to execute the tests.
- All tests execute successfully without failures.

---

Software Engineering Course – Winter 2025–2026
University of Haifa
