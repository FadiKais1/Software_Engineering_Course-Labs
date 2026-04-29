import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the login screen (login.fxml).
 *
 * Extended from Lab 2 to support:
 * - Thread A (FailCounterThread): records failed login attempts per email
 * - Thread B (BlockCheckerThread): checks if a user is blocked before allowing login
 * - Automatic UI blocking after n failed attempts, with a t-second cooldown
 *
 * The LoginController receives n and t from Main via the LoginAttemptManager.
 */
public class LoginController {

    /** Text field for username (email) input. Injected by JavaFX. */
    @FXML
    private TextField usernameField;

    /** Password field for password input. Injected by JavaFX. */
    @FXML
    private PasswordField passwordField;

    /**
     * Label for displaying error or status messages inline.
     * Used for wrong credentials, blocked state, and countdown messages.
     */
    @FXML
    private Label errorLabel;

    /** Manages the list of valid users loaded from users.txt. */
    private UserManager userManager;

    /** Reference to the primary stage for scene switching. */
    private Stage stage;

    /**
     * Shared attempt manager — tracks fail counts and block state per email.
     * Passed in from Main after being initialized with n and t.
     */
    private LoginAttemptManager attemptManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAttemptManager(LoginAttemptManager attemptManager) {
        this.attemptManager = attemptManager;
    }

    /**
     * Handles the login button click event.
     *
     * Flow:
     * 1. Read username and password from input fields
     * 2. If credentials are wrong -> start Thread A to record the failure
     *    - If this was the nth failure, show blocked message and start cooldown
     * 3. If credentials are correct -> start Thread B to check if user is blocked
     *    - If blocked -> show remaining block time
     *    - If not blocked -> open Welcome screen
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        User user = userManager.getUser(username, password);

        if (user == null) {
            // Wrong credentials — run Thread A to record the failure
            FailCounterThread failThread = new FailCounterThread(username, attemptManager);
            failThread.start();

            try {
                failThread.join(); // Wait for Thread A to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            int failCount = attemptManager.getFailCount(username);
            int maxAttempts = attemptManager.getMaxAttempts();

            if (attemptManager.isBlocked(username)) {
                long remaining = attemptManager.getRemainingBlockSeconds(username);
                errorLabel.setText("Too many failed attempts. Blocked for " + remaining + " seconds.");
                disableLoginTemporarily(username);
            } else {
                int attemptsLeft = maxAttempts - failCount;
                errorLabel.setText("Wrong credentials. " + attemptsLeft + " attempt(s) remaining.");
            }

            return;
        }

        // Correct credentials — run Thread B to check block status
        BlockCheckerThread blockThread = new BlockCheckerThread(username, attemptManager);
        blockThread.start();

        try {
            blockThread.join(); // Wait for Thread B to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (blockThread.isBlocked()) {
            long remaining = attemptManager.getRemainingBlockSeconds(username);
            errorLabel.setText("Account is blocked. Try again in " + remaining + " seconds.");
            return;
        }

        // Valid login and not blocked — open Welcome screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml"));
            Scene scene = new Scene(loader.load());

            WelcomeController controller = loader.getController();
            controller.setWelcomeMessage("Welcome " + user.getUsername());

            stage.setScene(scene);
            stage.setTitle("Welcome");

        } catch (Exception e) {
            errorLabel.setText("Failed to load welcome screen.");
            e.printStackTrace();
        }
    }

    /**
     * Disables the login form and shows a live countdown during the block period.
     * Re-enables everything once the block expires.
     *
     * @param email the blocked email address
     */
    private void disableLoginTemporarily(String email) {
        usernameField.setDisable(true);
        passwordField.setDisable(true);

        Thread countdownThread = new Thread(() -> {
            while (attemptManager.isBlocked(email)) {
                long remaining = attemptManager.getRemainingBlockSeconds(email);
                Platform.runLater(() ->
                    errorLabel.setText("Blocked. Try again in " + remaining + " second(s).")
                );
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            Platform.runLater(() -> {
                usernameField.setDisable(false);
                passwordField.setDisable(false);
                usernameField.clear();
                passwordField.clear();
                errorLabel.setText("You may try again now.");
            });
        });

        countdownThread.setDaemon(true);
        countdownThread.start();
    }
}