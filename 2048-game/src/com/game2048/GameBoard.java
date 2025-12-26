package com.game2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private Tile[][] board;
    private int score;
    private int bestScore;
    private boolean gameWon;
    private boolean gameOver;
    private final Random random;

    private GameListener listener;

    public interface GameListener {
        void onScoreChanged(int score);
        void onGameStateChanged(boolean gameOver, boolean gameWon);
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public GameBoard() {
        board = new Tile[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE];
        random = new Random();
        score = 0;
        bestScore = 0;
        gameWon = false;
        gameOver = false;

        initializeBoard();
        for (int i = 0; i < GameConstants.INITIAL_TILES_COUNT; i++) {
            addRandomTile();
        }
    }

    private void initializeBoard() {
        for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
            for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
                board[row][col] = Tile.createEmpty(row, col);
            }
        }
    }

    public boolean addRandomTile() {
        List<int[]> emptyCells = new ArrayList<>();

        for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
            for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
                if (board[row][col].isEmpty()) {
                    emptyCells.add(new int[]{row, col});
                }
            }
        }

        if (emptyCells.isEmpty()) return false;

        int[] cell = emptyCells.get(random.nextInt(emptyCells.size()));
        int row = cell[0];
        int col = cell[1];
        int value = random.nextInt(10) < 9 ? 2 : 4;

        board[row][col] = Tile.createWithValue(value, row, col);
        return true;
    }

    public boolean move(int direction) {
        resetMergeFlags();
        boolean moved = false;

        switch (direction) {
            case 0: moved = moveUp(); break;
            case 1: moved = moveRight(); break;
            case 2: moved = moveDown(); break;
            case 3: moved = moveLeft(); break;
        }

        if (moved) {
            addRandomTile();
            checkGameWon();
            checkGameOver();
            if (listener != null) {
                listener.onScoreChanged(score);
            }
        }

        return moved;
    }

    private boolean moveUp() {
        return moveTiles(true, true);
    }

    private boolean moveRight() {
        return moveTiles(false, false);
    }

    private boolean moveDown() {
        return moveTiles(true, false);
    }

    private boolean moveLeft() {
        return moveTiles(false, true);
    }

    private boolean moveTiles(boolean isVertical, boolean isForward) {
        boolean moved = false;
        int size = GameConstants.BOARD_SIZE;

        for (int i = 0; i < size; i++) {
            List<Tile> tiles = new ArrayList<>();

            for (int j = 0; j < size; j++) {
                int row = isVertical ? j : i;
                int col = isVertical ? i : j;
                if (!isForward) {
                    row = isVertical ? size - 1 - j : i;
                    col = isVertical ? i : size - 1 - j;
                }

                if (!board[row][col].isEmpty()) {
                    tiles.add(board[row][col]);
                }
            }

            List<Tile> merged = mergeTiles(tiles, i, isVertical, isForward);

            for (int j = 0; j < size; j++) {
                int row = isVertical ? j : i;
                int col = isVertical ? i : j;
                if (!isForward) {
                    row = isVertical ? size - 1 - j : i;
                    col = isVertical ? i : size - 1 - j;
                }

                if (j < merged.size()) {
                    Tile tile = merged.get(j);
                    if (board[row][col].getValue() != tile.getValue() ||
                            board[row][col].isEmpty() != tile.isEmpty()) {
                        moved = true;
                    }
                    board[row][col] = tile;
                    tile.setRow(row);
                    tile.setCol(col);
                } else {
                    if (!board[row][col].isEmpty()) {
                        moved = true;
                    }
                    board[row][col] = Tile.createEmpty(row, col);
                }
            }
        }

        return moved;
    }

    private List<Tile> mergeTiles(List<Tile> tiles, int lineIndex, boolean isVertical, boolean isForward) {
        List<Tile> result = new ArrayList<>();

        if (tiles.isEmpty()) {
            return result;
        }

        List<Tile> tempTiles = new ArrayList<>();
        for (Tile tile : tiles) {
            tempTiles.add(Tile.createWithValue(tile.getValue(), tile.getRow(), tile.getCol()));
        }

        for (int i = 0; i < tempTiles.size(); i++) {
            Tile current = tempTiles.get(i);

            if (i < tempTiles.size() - 1) {
                Tile next = tempTiles.get(i + 1);

                if (current.getValue() == next.getValue() && !current.isMerged() && !next.isMerged()) {
                    Tile mergedTile = Tile.createWithValue(current.getValue() * 2,
                            current.getRow(), current.getCol());
                    mergedTile.setMerged(true);
                    score += mergedTile.getValue();
                    if (score > bestScore) bestScore = score;
                    result.add(mergedTile);
                    i++;
                } else {
                    result.add(current);
                }
            } else {
                result.add(current);
            }
        }

        return result;
    }

    private void resetMergeFlags() {
        for (Tile[] row : board) {
            for (Tile tile : row) {
                tile.resetMergeFlag();
            }
        }
    }

    private void checkGameWon() {
        if (gameWon) return;

        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (tile.getValue() >= GameConstants.WINNING_VALUE) {
                    gameWon = true;
                    if (listener != null) {
                        listener.onGameStateChanged(gameOver, gameWon);
                    }
                    return;
                }
            }
        }
    }

    private void checkGameOver() {
        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (tile.isEmpty()) {
                    gameOver = false;
                    return;
                }
            }
        }

        int size = GameConstants.BOARD_SIZE;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int current = board[i][j].getValue();
                if (j < size - 1 && current == board[i][j + 1].getValue()) {
                    gameOver = false;
                    return;
                }
                if (i < size - 1 && current == board[i + 1][j].getValue()) {
                    gameOver = false;
                    return;
                }
            }
        }

        gameOver = true;
        if (listener != null) {
            listener.onGameStateChanged(gameOver, gameWon);
        }
    }

    public void reset() {
        score = 0;
        gameWon = false;
        gameOver = false;
        initializeBoard();

        for (int i = 0; i < GameConstants.INITIAL_TILES_COUNT; i++) {
            addRandomTile();
        }

        if (listener != null) {
            listener.onScoreChanged(score);
            listener.onGameStateChanged(gameOver, gameWon);
        }
    }

    public Tile[][] getBoard() { return board; }
    public int getScore() { return score; }
    public int getBestScore() { return bestScore; }
    public boolean isGameWon() { return gameWon; }
    public boolean isGameOver() { return gameOver; }
}