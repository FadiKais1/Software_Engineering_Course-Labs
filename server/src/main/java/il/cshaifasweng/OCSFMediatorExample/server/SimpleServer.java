

package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageType;
import il.cshaifasweng.OCSFMediatorExample.entities.TicTacToeMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.Random;

public class SimpleServer extends AbstractServer {

    private ConnectionToClient playerX;
    private ConnectionToClient playerO;

    private char[][] board;
    private char currentTurn;
    private boolean gameStarted;

    private final Random random = new Random();

    public SimpleServer(int port) {
        super(port);
        initBoard();
    }

    private void initBoard() {
        board = new char[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    @Override
    protected synchronized void clientConnected(ConnectionToClient client) {
        try {
            if (playerX != null && playerO != null) {
                TicTacToeMessage message =
                        new TicTacToeMessage(MessageType.ERROR, "Game is already full.");
                client.sendToClient(message);
                client.close();
                return;
            }

            if (playerX == null && playerO == null) {
                boolean firstPlayerIsX = random.nextBoolean();

                if (firstPlayerIsX) {
                    playerX = client;
                } else {
                    playerO = client;
                }

                TicTacToeMessage message =
                        new TicTacToeMessage(MessageType.WAITING_FOR_PLAYER,
                                "Waiting for another player...");
                client.sendToClient(message);
                return;
            }

            if (playerX == null) {
                playerX = client;
            } else {
                playerO = client;
            }

            startGame();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void startGame() throws IOException {
        initBoard();
        gameStarted = true;
        currentTurn = random.nextBoolean() ? 'X' : 'O';

        sendGameStart(playerX, 'X');
        sendGameStart(playerO, 'O');
    }

    private void sendGameStart(ConnectionToClient client, char symbol) throws IOException {
        TicTacToeMessage message = new TicTacToeMessage(MessageType.GAME_START);

        message.setBoard(copyBoard());
        message.setPlayerSymbol(symbol);
        message.setCurrentTurn(currentTurn);

        if (currentTurn == symbol) {
            message.setText("Game started. You are " + symbol + ". Your turn.");
        } else {
            message.setText("Game started. You are " + symbol + ". Opponent's turn.");
        }

        client.sendToClient(message);
    }

    @Override
    protected synchronized void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (!(msg instanceof TicTacToeMessage)) {
            return;
        }

        TicTacToeMessage message = (TicTacToeMessage) msg;

        try {
            if (message.getType() == MessageType.MAKE_MOVE) {
                handleMove(message, client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMove(TicTacToeMessage message, ConnectionToClient client) throws IOException {
        if (!gameStarted) {
            sendInvalidMove(client, "Game has not started yet.");
            return;
        }

        char symbol = getSymbolOfClient(client);

        if (symbol == '?') {
            sendInvalidMove(client, "You are not part of this game.");
            return;
        }

        if (symbol != currentTurn) {
            sendInvalidMove(client, "It is not your turn.");
            return;
        }

        int row = message.getRow();
        int col = message.getCol();

        if (!isValidCell(row, col)) {
            sendInvalidMove(client, "Invalid cell.");
            return;
        }

        if (board[row][col] != ' ') {
            sendInvalidMove(client, "This cell is already taken.");
            return;
        }

        board[row][col] = symbol;

        Character winner = checkWinner();

        if (winner != null) {
            gameStarted = false;
            sendGameOver(winner);
            return;
        }

        if (isDraw()) {
            gameStarted = false;
            sendGameOver(null);
            return;
        }

        switchTurn();
        sendBoardUpdate();
    }

    private char getSymbolOfClient(ConnectionToClient client) {
        if (client == playerX) {
            return 'X';
        }

        if (client == playerO) {
            return 'O';
        }

        return '?';
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }

    private void switchTurn() {
        if (currentTurn == 'X') {
            currentTurn = 'O';
        } else {
            currentTurn = 'X';
        }
    }

    private void sendInvalidMove(ConnectionToClient client, String text) throws IOException {
        TicTacToeMessage message = new TicTacToeMessage(MessageType.INVALID_MOVE, text);

        message.setBoard(copyBoard());
        message.setCurrentTurn(currentTurn);

        client.sendToClient(message);
    }

    private void sendBoardUpdate() throws IOException {
        TicTacToeMessage message = new TicTacToeMessage(MessageType.BOARD_UPDATE);

        message.setBoard(copyBoard());
        message.setCurrentTurn(currentTurn);
        message.setText("Board updated.");

        sendToPlayers(message);
    }

    private void sendGameOver(Character winner) throws IOException {
        TicTacToeMessage message = new TicTacToeMessage(MessageType.GAME_OVER);

        message.setBoard(copyBoard());
        message.setGameOver(true);
        message.setWinner(winner);

        if (winner == null) {
            message.setText("Game over. It is a draw.");
        } else {
            message.setText("Game over. Winner is " + winner + ".");
        }

        sendToPlayers(message);
    }

    private void sendToPlayers(TicTacToeMessage message) throws IOException {
        if (playerX != null) {
            playerX.sendToClient(message);
        }

        if (playerO != null) {
            playerO.sendToClient(message);
        }
    }

    private Character checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' &&
                    board[i][0] == board[i][1] &&
                    board[i][1] == board[i][2]) {
                return board[i][0];
            }

            if (board[0][i] != ' ' &&
                    board[0][i] == board[1][i] &&
                    board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }

        if (board[0][0] != ' ' &&
                board[0][0] == board[1][1] &&
                board[1][1] == board[2][2]) {
            return board[0][0];
        }

        if (board[0][2] != ' ' &&
                board[0][2] == board[1][1] &&
                board[1][1] == board[2][0]) {
            return board[0][2];
        }

        return null;
    }

    private boolean isDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    private char[][] copyBoard() {
        char[][] copy = new char[3][3];

        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 3);
        }

        return copy;
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        try {
            TicTacToeMessage message =
                    new TicTacToeMessage(MessageType.PLAYER_DISCONNECTED,
                            "The other player disconnected. Game stopped.");

            if (client == playerX && playerO != null) {
                playerO.sendToClient(message);
            }

            if (client == playerO && playerX != null) {
                playerX.sendToClient(message);
            }

            playerX = null;
            playerO = null;
            gameStarted = false;
            initBoard();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}