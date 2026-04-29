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
 * Handles user input from the username and password fields,
 * validates credentials against the loaded user list,
 * and transitions to the welcome screen on successful login.
 */
public class LoginController {

    /** Text field for the username (email) input. Injected by JavaFX. */
    @FXML
    private TextField usernameField;

    /** Password field for the password input. Injected by JavaFX. */
    @FXML
    private PasswordField passwordField;

    /**
     * Label used to display error messages inline (e.g. wrong credentials).
     * No pop-ups are used — errors appear directly in the login screen.
     */
    @FXML
    private Label errorLabel;

    /** Manages the list of valid users loaded from users.txt. */
    private UserManager userManager;

    /** Reference to the primary stage, used to switch scenes. */
    private Stage stage;

    /**
     * Sets the UserManager instance used to validate login credentials.
     *
     * @param userManager the UserManager containing the loaded valid users
     */
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Sets the primary Stage reference so this controller can switch scenes.
     *
     * @param stage the primary application window
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the login button click event.
     *
     * Reads the username and password from the input fields and checks
     * them against the user list via UserManager. If credentials are valid,
     * loads and displays the welcome screen. Otherwise, shows an error message
     * inline without using any pop-up dialog.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Look up user by username and password
        User user = userManager.getUser(username, password);

        // If no matching user found, show error in the label and stop
        if (user == null) {
            errorLabel.setText("user or password do not match");
            return;
        }

        // Valid login — load and switch to the welcome screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml"));
            Scene scene = new Scene(loader.load());

            // Pass the welcome message to the welcome screen controller
            WelcomeController controller = loader.getController();
            controller.setWelcomeMessage("Welcome " + user.getUsername());

            // Replace the current scene with the welcome scene
            stage.setScene(scene);
            stage.setTitle("Welcome");

        } catch (Exception e) {
            errorLabel.setText("Failed to load welcome screen");
            e.printStackTrace();
        }
    }
}