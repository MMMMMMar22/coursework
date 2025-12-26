package com.game2048;

import javax.swing.*;

public class Game {
    private JFrame frame;
    private final GamePanel panel;
    private final GameBoard board;
    private boolean paused = false;

    public Game() {
        board = new GameBoard();
        panel = new GamePanel(board, this);
        setupFrame();
    }

    private void setupFrame() {
        frame = new JFrame("2048");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    public void start() {
        frame.setVisible(true);
        panel.requestFocusInWindow();
    }

    public void togglePause() {
        paused = !paused;
        panel.setPaused(paused);
    }

    public void restart() {
        board.reset();
        panel.reset();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.start();
        });
    }
}