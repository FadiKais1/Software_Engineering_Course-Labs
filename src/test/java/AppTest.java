import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.matcher.control.LabeledMatchers;

@ExtendWith(ApplicationExtension.class)
public class AppTest {

    @Start
    private void start(Stage stage) throws Exception {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/login.fxml"));

        Parent root = loader.load();

        LoginController controller = loader.getController();

        controller.setStage(stage);

        controller.setUserManager(new UserManager());

        controller.setAttemptManager(new LoginAttemptManager(3,5));

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void usernameInitiallyEmpty(FxRobot robot) {

        FxAssert.verifyThat(
                "#usernameField",
                TextInputControlMatchers.hasText(""));
    }

    @Test
    void passwordInitiallyEmpty(FxRobot robot) {

        FxAssert.verifyThat(
                "#passwordField",
                TextInputControlMatchers.hasText(""));
    }

    @Test
    void loginButtonExists(FxRobot robot) {

        FxAssert.verifyThat(
                ".button",
                LabeledMatchers.hasText("Login"));
    }

    @Test
    void wrongLoginShowsError(FxRobot robot) {

        robot.clickOn("#usernameField")
                .write("wrong@email.com");

        robot.clickOn("#passwordField")
                .write("123456");

        robot.clickOn(".button");

        FxAssert.verifyThat(
                "#errorLabel",
                LabeledMatchers.hasText(
                        "Wrong credentials. 2 attempt(s) remaining."));
    }
}