import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point of the JavaFX application.
 *
 * Responsible for initializing the UserManager, loading the login screen,
 * wiring up the controller, and launching the primary application window.
 */
public class Main extends Application {

    /**
     * JavaFX lifecycle method called when the application starts.
     *
     * Loads valid users from users.txt, sets up the login scene,
     * and configures the primary stage. Also registers a close handler
     * so that closing the window terminates the entire application.
     *
     * @param primaryStage the main window provided by the JavaFX runtime
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the user manager and load users from file
        UserManager userManager = new UserManager();
        userManager.loadUsers("users.txt"); // users.txt must be in the project root

        // Load the login screen FXML layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader.load());

        // Wire the controller with the stage and user manager
        LoginController controller = loader.getController();
        controller.setStage(primaryStage);
        controller.setUserManager(userManager);

        // Configure and display the primary stage
        primaryStage.setTitle("Users Login");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Ensure the application fully exits when the window is closed
        primaryStage.setOnCloseRequest(event -> {
            javafx.application.Platform.exit();
            System.exit(0);
        });
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