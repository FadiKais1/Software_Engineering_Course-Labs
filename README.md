# Lab 3 - Thread-Based Login Security System
**Course:** Software Engineering - Winter 2025-26  
**Authors:** Fadi Kais, Yosef Salem  
**Branch:** lab3 (based on lab2)

---

## 📌 Overview

This project extends the JavaFX login system from Lab 2 by adding a thread-based authentication security mechanism.

The application now supports:
- A configurable maximum number of failed login attempts (`n`)
- A configurable block duration in seconds (`t`)
- Per-user attempt tracking using two dedicated threads
- Automatic blocking and unblocking with a live countdown timer

---

## 🗂️ Project Structure

```
lab2/
├── src/
│   └── main/
│       ├── java/
│       │   ├── Main.java                  # Entry point + setup dialog for n and t
│       │   ├── User.java                  # User model with validation logic
│       │   ├── UserManager.java           # Loads and manages users from file
│       │   ├── LoginController.java       # Login screen controller (updated with threads)
│       │   ├── WelcomeController.java     # Welcome screen controller
│       │   ├── LoginAttemptManager.java   # Tracks fail counts and block state per user
│       │   ├── FailCounterThread.java     # Thread A - records failed attempts
│       │   └── BlockCheckerThread.java    # Thread B - checks if user is blocked
│       └── resources/
│           ├── login.fxml                 # Login screen layout
│           └── welcome.fxml              # Welcome screen layout
├── users.txt                              # Input file with user credentials
├── pom.xml                                # Maven build configuration
└── commandtorun.txt                       # Command to run the project
```

---

## 🧩 Class Design

### New classes (Lab 3)

**LoginAttemptManager**
The core of the security system. Tracks per-email failed attempt counts and block timestamps using two synchronized HashMaps. All methods are synchronized to prevent race conditions when accessed by multiple threads simultaneously.

**FailCounterThread (Thread A)**
A thread created and started every time a user enters wrong credentials. It calls LoginAttemptManager.recordFailedAttempt() to increment the fail count for that email. If the count reaches n, the manager automatically blocks that email.

**BlockCheckerThread (Thread B)**
A thread created and started when the user enters correct credentials. It calls LoginAttemptManager.isBlocked() to verify whether the user is currently blocked. The result is stored and read by LoginController after join() completes.

### Modified classes (from Lab 2)

**Main**
Now shows a setup dialog before the login screen opens, asking for n (max failed attempts) and t (block duration in seconds). Initializes the LoginAttemptManager with these values and passes it to LoginController.

**LoginController**
Extended to launch Thread A on failed attempts, Thread B on correct credentials, display countdown inline, and disable/re-enable input fields automatically.

---

## 🔄 Application Flow

1. App starts → setup dialog asks for n and t
2. Login screen opens
3. User enters credentials:
   - Wrong → Thread A runs → fail count incremented
     - If fail count < n → show attempts remaining
     - If fail count = n → block user, show countdown, disable fields
   - Correct → Thread B runs → checks if blocked
     - If blocked → show remaining block time
     - If not blocked → open Welcome screen
4. After t seconds → block expires → fields re-enabled → user can try n more times

---

## ▶️ How to Run

```bash
mvn clean javafx:run
```

A setup dialog will appear first. Enter n and t, then click Start.

---

## 🧵 Thread Safety

LoginAttemptManager uses synchronized methods to ensure Thread A and Thread B cannot corrupt shared data concurrently. LoginController calls thread.join() after starting each thread to wait for the result before updating the UI.

---

## 🛠️ Technologies Used

- Java / JavaFX
- Java Threads (extends Thread)
- Thread synchronization (synchronized)
- Maven
