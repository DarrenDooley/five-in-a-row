package com.genesys.darrendooley;

import com.google.common.annotations.VisibleForTesting;

import java.util.Arrays;
import java.util.HashMap;

public class Grid {

    private static HashMap<String, Boolean> playersTurn = new HashMap<>(2);
    private static HashMap<String, Character> playersShape = new HashMap<>(2);
    private final static String fiveOs = "ooooo";
    private final static String fiveXs = "xxxxx";

    private final int numRows = 6;
    private final int numCols = 9;
    private char[][] grid;
    private String winner;
    private boolean gameWon = false;
    private boolean gameDrawn = false;
    private boolean playerLeft = false;

    public Grid() {
        grid = new char[numRows][numCols];
        this.intialiseGridState();
    }

    public void intialiseGridState() {
        for (int row = 0; row < numRows; row++) {
            Arrays.fill(grid[row] = new char[numCols], ' ');
        }
    }

    public String getState(String name) {
        StringBuilder gridStringBuilder = new StringBuilder();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                gridStringBuilder.append("[" + grid[row][col] + "]");

                if (col > 0 && col == numCols - 1) {
                    gridStringBuilder.append('\n');
                }
            }
        }
        if (gameWon) {
            gridStringBuilder.append(String.format("\n'%s' has won the game!", winner));
        } else if (gameDrawn) {
            gridStringBuilder.append("\nThe game has ended as a draw!");
        } else if (playerLeft) {
            gridStringBuilder.append("\nThe other player has left the game. You win!");
        } else {
            gridStringBuilder.append(getTurn(name));
        }
        return gridStringBuilder.toString();
    }

    public String getTurn(String name) {
        String turnString = "An error has occurred.";
        if (playersTurn.get(name)) {
            turnString = String.format("It's your turn '%s'. Please enter a number (1-9):", name);
        } else {
            turnString = "The opponent is making their move.";
        }
        return turnString;
    }

    public String addPlayer(String name) {
        if (playersTurn.size() >= 2) return "Sorry, the game is full.";

        if (playersTurn.size() == 0) {
            playersTurn.put(name, true);
            playersShape.put(name, 'x');
        } else {
            if (playersTurn.containsKey(name)) return String.format("Username '%s' has been taken.", name);
            playersTurn.put(name, false);
            playersShape.put(name, 'o');
        }
        return String.format("'%s' has joined the game.", name);
    }

    public String removePlayer(String name) {
        playersTurn.remove(name);
        playersShape.remove(name);
        if (!isGameOver()) {
            playersTurn.forEach((k, v) -> winner = k);  // the player remaining in the hashmap wins
        }
        playerLeft = true;
        return String.format("'%s' has left the game.", name);
    }

    public String dropDisc(String name, int columnNum) {
        if (!playersTurn.get(name)) return "Please wait for your opponent to make a move.";
        if (!isColumnInGrid(columnNum)) return "You missed the grid! Please enter a number (1-9):";
        if (!isSpaceAvailable(columnNum)) return "That column is full! Please enter a number (1-9):";
        if (isGameOver()) return getState(name);

        int selectedRow = 0;
        int column = columnNum - 1;
        char shape;
        shape = playersShape.get(name);
            for (int row = 0; row < numRows; row++) {
                if (grid[row][column] == 'x' || grid[row][column] == 'o') {
                    selectedRow = row - 1;
                    grid[selectedRow][column] = shape;
                    rotatePlayerTurns();
                    break;
                }
                if (row == numRows - 1) {
                    selectedRow = row;
                    grid[selectedRow][column] = shape;
                    rotatePlayerTurns();
                }
            }

        if (isWinner(selectedRow, column, shape)) {
            winner = name;
            gameWon = true;
            return getState(name);
        }
        if (isDraw()) {
            gameDrawn = true;
            return getState(name);
        }
        return getState(name);
    }

    public boolean isColumnInGrid(int columnNum) {
        return columnNum >= 1 && columnNum <= 9;
    }

    public boolean isSpaceAvailable(int columnNum) {
        return grid[0][columnNum - 1] != 'x' && grid[0][columnNum - 1] != 'o';
    }

    public void rotatePlayerTurns() {
        playersTurn.forEach((k, v) -> playersTurn.put(k, !v));
    }

    public boolean isDraw() {
        for (int col = 0; col < numCols; col++) {
            if (grid[0][col] == ' ') return false;
        }
        return true;
    }

    public boolean isWinner(int row, int col, char shape) {
        if (checkVerticalWin(row, col, shape)
                || checkHorizontalWin(row, shape)
                || checkRightDiagonalWin(row, col, shape)
                || checkLeftDiagonalWin(row, col, shape)) {
            return true;
        }
        return false;
    }

    public boolean checkVerticalWin(int row, int col, char shape) {
        StringBuilder verticalLine = new StringBuilder(numRows - row + 1);
        for (int i = row; i < numRows; i++) {
                verticalLine.append(grid[i][col]);
        }
        return has5InARow(shape, verticalLine.toString());
    }

    public boolean checkHorizontalWin(int row, char shape) {
        StringBuilder horizontalLine = new StringBuilder(numCols);
        for (int i = 0; i < numCols; i++) {
            horizontalLine.append(grid[row][i]);
        }
        return has5InARow(shape, horizontalLine.toString());

    }

    public boolean checkRightDiagonalWin(int row, int col, char shape) {
        StringBuilder diagonalLine = new StringBuilder(numRows);
        for (int i = 0; i < numRows; i++) {
            int j = row + col - i;
            if (j >= 0 && j < numCols) {
                diagonalLine.append(grid[i][j]);
            }
        }
        return has5InARow(shape, diagonalLine.toString());
    }

    public boolean checkLeftDiagonalWin(int row, int col, char shape) {
        StringBuilder diagonalLine = new StringBuilder(numRows);
        for (int i = 0; i < numRows; i++) {
            int j = col - row + i;
            if (j >= 0 && j < numCols) {
                diagonalLine.append(grid[i][j]);
            }
        }
        return has5InARow(shape, diagonalLine.toString());
    }

    public boolean has5InARow(char shape, String line) {
        if (shape == 'x') {
            return line.contains(fiveXs);
        } else {
            return line.contains(fiveOs);
        }
    }

    public boolean doesPlayerExist(String name) {
        return playersTurn.containsKey(name);
    }

    public boolean hasTwoPlayers() {
        return playersTurn.size() == 2;
    }

    public boolean hasAnyPlayers() {
        return playersTurn.size() != 0;
    }

    public boolean isGameOver() {
        if (gameWon || gameDrawn || playerLeft) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public void setGrid(char[][] grid) {
        this.grid = grid;
    }
}
