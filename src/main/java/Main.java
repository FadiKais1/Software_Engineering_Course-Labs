import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Entry point of the JavaFX application (Lab 3).
 *
 * Extended from Lab 2 to:
 * - Show a setup dialog before the login screen asking for:
 *     n = maximum failed login attempts before blocking
 *     t = block duration in seconds
 * - Initialize a LoginAttemptManager with those values
 * - Pass the LoginAttemptManager to the LoginController
 */
public class Main extends Application {

    /**
     * JavaFX lifecycle method called when the application starts.
     *
     * Shows a setup dialog to collect n and t, then initializes
     * the LoginAttemptManager and launches the login screen.
     *
     * @param primaryStage the main application window
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Show setup dialog to get n and t before opening the login screen
        int[] params = showSetupDialog();

        if (params == null) {
            // User closed the dialog without confirming — exit app
            javafx.application.Platform.exit();
            return;
        }

        int n = params[0]; // max failed attempts
        int t = params[1]; // block duration in seconds

        // Initialize the shared attempt manager with n and t
        LoginAttemptManager attemptManager = new LoginAttemptManager(n, t * 1000L);

        // Load users from file
        UserManager userManager = new UserManager();
        userManager.loadUsers("users.txt");

        // Load the login screen FXML layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader.load());

        // Wire the controller with all required dependencies
        LoginController controller = loader.getController();
        controller.setStage(primaryStage);
        controller.setUserManager(userManager);
        controller.setAttemptManager(attemptManager);

        // Configure and show the primary window
        primaryStage.setTitle("Users Login");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Ensure full exit when window is closed
        primaryStage.setOnCloseRequest(event -> {
            javafx.application.Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Displays a setup dialog asking the user to enter n and t.
     *
     * The dialog appears before the login screen opens.
     * It validates that both inputs are positive integers.
     * If the user cancels, null is returned and the app exits.
     *
     * @return an int array [n, t], or null if the user cancelled
     */
    private int[] showSetupDialog() {
        // Build the dialog layout
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Login Security Setup");
        dialog.setHeaderText("Configure login attempt limits before starting.");

        // Add OK and Cancel buttons
        ButtonType okButton = new ButtonType("Start", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        // Create input fields
        TextField nField = new TextField("3");
        TextField tField = new TextField("10");

        // Layout the fields in a grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Max failed attempts (n):"), 0, 0);
        grid.add(nField, 1, 0);
        grid.add(new Label("Block duration in seconds (t):"), 0, 1);
        grid.add(tField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result when OK is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                try {
                    int n = Integer.parseInt(nField.getText().trim());
                    int t = Integer.parseInt(tField.getText().trim());

                    if (n <= 0 || t <= 0) {
                        showError("Both n and t must be positive integers.");
                        return null;
                    }

                    return new int[]{n, t};
                } catch (NumberFormatException e) {
                    showError("Please enter valid integers for n and t.");
                    return null;
                }
            }
            return null;
        });

        Optional<int[]> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Displays a simple error alert with the given message.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Main method — launches the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}