# Lab 2 - JavaFX Login System
**Course:** Software Engineering - Winter 2025-26  
**Authors:** Fadi Kais, Yosef Salem

---

## 📌 Overview

This project extends the user validation system from Lab 1 by adding a full graphical user interface (GUI) built with JavaFX.  
The application allows a user to log in through a login screen, validates their credentials against a list of registered users loaded from a file, and displays a personalized welcome screen upon successful login.

---

## 🗂️ Project Structure

```
lab2/
├── src/
│   └── main/
│       ├── java/
│       │   ├── Main.java               # Application entry point
│       │   ├── User.java               # User model with validation logic
│       │   ├── UserManager.java        # Loads and manages users from file
│       │   ├── LoginController.java    # Controls the login screen behavior
│       │   └── WelcomeController.java  # Controls the welcome screen behavior
│       └── resources/
│           ├── login.fxml              # Login screen layout (SceneBuilder)
│           └── welcome.fxml            # Welcome screen layout (SceneBuilder)
├── users.txt                           # Input file containing user credentials
├── pom.xml                             # Maven build configuration
└── commandtorun.txt                    # Command to run the project
```

---

## 🧩 Class Design

### `User`
Represents a single system user. The constructor validates both the username (email) and password before creating the object. If either is invalid, an `IllegalArgumentException` is thrown. Fields are private and accessed only through getters (encapsulation).

### `UserManager`
Responsible for reading `users.txt` and building an `ArrayList` of valid `User` objects. Invalid entries are silently skipped. Also provides methods to look up a user by username and password for login verification.

### `Main`
The JavaFX application entry point. Initializes the `UserManager`, loads the login screen FXML, wires up the `LoginController`, and launches the primary window. Handles the window close event to ensure the application exits cleanly.

### `LoginController`
Controls the login screen (`login.fxml`). On login button click, it reads the input fields and checks credentials via `UserManager`. If invalid, it displays an inline error message (no pop-ups). If valid, it loads `welcome.fxml` and switches the scene.

### `WelcomeController`
Controls the welcome screen (`welcome.fxml`). Receives a personalized message from `LoginController` after successful login and displays it to the user.

---

## ▶️ How to Run

Make sure you have **Maven** and **Java** installed.

```bash
mvn clean javafx:run
```

> ⚠️ Make sure `users.txt` is located in the project root folder before running.

---

## 📂 Input File Format

Each line in `users.txt` must follow this format:
```
username,password
```

**Example:**
```
student@haifa.ac.il,Pass123!
hello@gmail.com,Abc123#@
gooduser@test.com,Test12$
```

Only valid entries are loaded. A valid username is a properly formatted email address (max 50 characters), and a valid password is 8–12 characters containing at least one letter, one digit, and one symbol.

---

## 🖥️ Application Flow

1. App starts → loads `users.txt` → builds list of valid users
2. Login screen opens → user enters username and password
3. **Wrong credentials** → inline error message shown in the login screen (no pop-up)
4. **Correct credentials** → login screen replaced with a Welcome screen showing a personalized message
5. Closing any window → application exits completely

---

## 🛠️ Technologies Used

- Java
- JavaFX (GUI framework)
- FXML + SceneBuilder (UI layout)
- Maven (build tool)
- OOP principles (encapsulation, separation of concerns)
