import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the welcome screen (welcome.fxml).
 *
 * Displayed after a successful login. Shows a personalized
 * welcome message to the authenticated user.
 */
public class WelcomeController {

    /**
     * Label that displays the welcome message.
     * Its text is set programmatically by the LoginController
     * after a successful login, using the authenticated user's name.
     */
    @FXML
    private Label welcomeLabel;

    /**
     * Sets the welcome message displayed on screen.
     *
     * Called by LoginController after a successful login,
     * passing a message such as "Welcome user@example.com".
     *
     * @param message the personalized welcome text to display
     */
    public void setWelcomeMessage(String message) {
        welcomeLabel.setText(message);
    }
}