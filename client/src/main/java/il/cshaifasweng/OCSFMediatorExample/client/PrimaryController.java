package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.TicTacToeMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PrimaryController {

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private Button connectButton;

    @FXML
    private Label symbolLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button cell00;

    @FXML
    private Button cell01;

    @FXML
    private Button cell02;

    @FXML
    private Button cell10;

    @FXML
    private Button cell11;

    @FXML
    private Button cell12;

    @FXML
    private Button cell20;

    @FXML
    private Button cell21;

    @FXML
    private Button cell22;

    private Button[][] cells;

    private char mySymbol = ' ';
    private char currentTurn = ' ';
    private boolean gameStarted = false;

    @FXML
    public void initialize() {
        cells = new Button[][] {
                {cell00, cell01, cell02},
                {cell10, cell11, cell12},
                {cell20, cell21, cell22}
        };

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        disableBoard(true);
        statusLabel.setText("Not connected.");
        symbolLabel.setText("You are: -");
    }

    @FXML
    private void onConnectClicked() {
        try {
            String host = hostField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());

            SimpleClient client = SimpleClient.getClient();
            client.setHost(host);
            client.setPort(port);
            client.openConnection();

            connectButton.setDisable(true);
            hostField.setDisable(true);
            portField.setDisable(true);
            statusLabel.setText("Connected. Waiting for server response...");

        } catch (Exception e) {
            statusLabel.setText("Connection failed: " + e.getMessage());
        }
    }

    @FXML
    private void onCell00Clicked() {
        handleCellClick(0, 0);
    }

    @FXML
    private void onCell01Clicked() {
        handleCellClick(0, 1);
    }

    @FXML
    private void onCell02Clicked() {
        handleCellClick(0, 2);
    }

    @FXML
    private void onCell10Clicked() {
        handleCellClick(1, 0);
    }

    @FXML
    private void onCell11Clicked() {
        handleCellClick(1, 1);
    }

    @FXML
    private void onCell12Clicked() {
        handleCellClick(1, 2);
    }

    @FXML
    private void onCell20Clicked() {
        handleCellClick(2, 0);
    }

    @FXML
    private void onCell21Clicked() {
        handleCellClick(2, 1);
    }

    @FXML
    private void onCell22Clicked() {
        handleCellClick(2, 2);
    }

    private void handleCellClick(int row, int col) {
        try {
            if (!gameStarted) {
                statusLabel.setText("Game has not started yet.");
                return;
            }

            if (mySymbol != currentTurn) {
                statusLabel.setText("Not your turn.");
                return;
            }

            if (!cells[row][col].getText().isEmpty()) {
                statusLabel.setText("This cell is already taken.");
                return;
            }

            TicTacToeMessage message = new TicTacToeMessage(MessageType.MAKE_MOVE);
            message.setRow(row);
            message.setCol(col);

            SimpleClient.getClient().sendToServer(message);

        } catch (Exception e) {
            statusLabel.setText("Failed to send move: " + e.getMessage());
        }
    }

    @Subscribe
    public void onTicTacToeEvent(TicTacToeEvent event) {
        Platform.runLater(() -> handleServerMessage(event.getMessage()));
    }

    private void handleServerMessage(TicTacToeMessage message) {
        switch (message.getType()) {
            case WAITING_FOR_PLAYER:
                statusLabel.setText(message.getText());
                disableBoard(true);
                break;

            case GAME_START:
                gameStarted = true;
                mySymbol = message.getPlayerSymbol();
                currentTurn = message.getCurrentTurn();

                symbolLabel.setText("You are: " + mySymbol);
                updateBoard(message.getBoard());
                updateTurnText();
                break;

            case BOARD_UPDATE:
                currentTurn = message.getCurrentTurn();
                updateBoard(message.getBoard());
                updateTurnText();
                break;

            case INVALID_MOVE:
                statusLabel.setText(message.getText());
                updateBoard(message.getBoard());
                updateTurnText();
                break;

            case GAME_OVER:
                gameStarted = false;
                updateBoard(message.getBoard());
                statusLabel.setText(message.getText());
                disableBoard(true);
                break;

            case PLAYER_DISCONNECTED:
                gameStarted = false;
                statusLabel.setText(message.getText());
                disableBoard(true);
                break;

            case ERROR:
                statusLabel.setText(message.getText());
                disableBoard(true);
                break;

            default:
                break;
        }
    }

    private void updateBoard(char[][] board) {
        if (board == null) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char value = board[i][j];

                if (value == ' ') {
                    cells[i][j].setText("");
                } else {
                    cells[i][j].setText(String.valueOf(value));
                }
            }
        }
    }

    private void updateTurnText() {
        if (!gameStarted) {
            disableBoard(true);
            return;
        }

        if (currentTurn == mySymbol) {
            statusLabel.setText("Your turn.");
            disableBoard(false);
        } else {
            statusLabel.setText("Opponent's turn.");
            disableBoard(true);
        }
    }

    private void disableBoard(boolean disabled) {
        if (cells == null) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setDisable(disabled);
            }
        }
    }
}