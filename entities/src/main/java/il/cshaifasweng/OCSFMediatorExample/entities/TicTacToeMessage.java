package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class TicTacToeMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private MessageType type;
    private char[][] board;
    private int row;
    private int col;
    private char playerSymbol;
    private char currentTurn;
    private String text;
    private boolean gameOver;
    private Character winner;

    public TicTacToeMessage(MessageType type) {
        this.type = type;
    }

    public TicTacToeMessage(MessageType type, String text) {
        this.type = type;
        this.text = text;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public char getPlayerSymbol() {
        return playerSymbol;
    }

    public void setPlayerSymbol(char playerSymbol) {
        this.playerSymbol = playerSymbol;
    }

    public char getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(char currentTurn) {
        this.currentTurn = currentTurn;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Character getWinner() {
        return winner;
    }

    public void setWinner(Character winner) {
        this.winner = winner;
    }
}