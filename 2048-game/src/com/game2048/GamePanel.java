package com.game2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final GameBoard board;
    private final Game game;
    private boolean paused = false;
    private boolean gameOver = false;
    private boolean gameWon = false;

    public GamePanel(GameBoard board, Game game) {
        this.board = board;
        this.game = game;

        board.setListener(new GameBoard.GameListener() {
            @Override
            public void onScoreChanged(int score) {
                repaint();
            }

            @Override
            public void onGameStateChanged(boolean gameOver, boolean gameWon) {
                GamePanel.this.gameOver = gameOver;
                GamePanel.this.gameWon = gameWon;
                repaint();
            }
        });

        setPreferredSize(new Dimension(
                GameConstants.WINDOW_WIDTH,
                GameConstants.WINDOW_HEIGHT
        ));
        setBackground(GameConstants.BACKGROUND_COLOR);
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        bindKey(im, am, KeyEvent.VK_UP, "up", 0);
        bindKey(im, am, KeyEvent.VK_W, "w_up", 0);
        bindKey(im, am, KeyEvent.VK_RIGHT, "right", 1);
        bindKey(im, am, KeyEvent.VK_D, "d_right", 1);
        bindKey(im, am, KeyEvent.VK_DOWN, "down", 2);
        bindKey(im, am, KeyEvent.VK_S, "s_down", 2);
        bindKey(im, am, KeyEvent.VK_LEFT, "left", 3);
        bindKey(im, am, KeyEvent.VK_A, "a_left", 3);

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "restart");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exit");

        am.put("pause", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                game.togglePause();
            }
        });

        am.put("restart", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                game.restart();
            }
        });

        am.put("exit", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void bindKey(InputMap im, ActionMap am, int keyCode, String actionName, int direction) {
        im.put(KeyStroke.getKeyStroke(keyCode, 0), actionName);
        am.put(actionName, new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleMove(direction);
            }
        });
    }

    private void handleMove(int direction) {
        if (paused || gameOver || gameWon) return;
        if (board.move(direction)) {
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGameBoard(g2d);
        drawInfoPanel(g2d);
        drawGameStatus(g2d);
    }

    private void drawGameBoard(Graphics2D g) {
        int offsetX = GameConstants.PANEL_PADDING;
        int offsetY = GameConstants.PANEL_PADDING;

        g.setColor(GameConstants.BOARD_COLOR);
        g.fillRoundRect(offsetX, offsetY, GameConstants.GAME_PANEL_SIZE,
                GameConstants.GAME_PANEL_SIZE, 15, 15);

        Tile[][] tiles = board.getBoard();
        for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
            for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
                drawTile(g, tiles[row][col], offsetX, offsetY);
            }
        }
    }

    private void drawTile(Graphics2D g, Tile tile, int offsetX, int offsetY) {
        int x = offsetX + tile.getCol() * (GameConstants.TILE_SIZE + GameConstants.TILE_MARGIN)
                + GameConstants.TILE_MARGIN;
        int y = offsetY + tile.getRow() * (GameConstants.TILE_SIZE + GameConstants.TILE_MARGIN)
                + GameConstants.TILE_MARGIN;

        if (tile.isEmpty()) {
            g.setColor(GameConstants.TILE_COLORS[0]);
            g.fillRoundRect(x, y, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, 10, 10);
            return;
        }

        g.setColor(tile.getColor());
        g.fillRoundRect(x, y, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, 10, 10);

        String valueText = String.valueOf(tile.getValue());
        g.setColor(tile.getTextColor());
        g.setFont(new Font("Arial", Font.BOLD, GameConstants.TILE_SIZE / 3));

        FontMetrics fm = g.getFontMetrics();
        int textX = x + (GameConstants.TILE_SIZE - fm.stringWidth(valueText)) / 2;
        int textY = y + (GameConstants.TILE_SIZE + fm.getAscent()) / 2 - fm.getDescent();

        g.drawString(valueText, textX, textY);
    }

    private void drawInfoPanel(Graphics2D g) {
        int infoX = GameConstants.PANEL_PADDING * 2 + GameConstants.GAME_PANEL_SIZE;
        int infoY = GameConstants.PANEL_PADDING;
        int width = GameConstants.INFO_PANEL_WIDTH;
        int height = GameConstants.GAME_PANEL_SIZE;

        g.setColor(GameConstants.INFO_PANEL_COLOR);
        g.fillRoundRect(infoX, infoY, width, height, 15, 15);

        g.setColor(GameConstants.DARK_TEXT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g.getFontMetrics();
        String title = "2048";
        g.drawString(title, infoX + (width - fm.stringWidth(title)) / 2, infoY + 50);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Score: " + board.getScore(), infoX + 20, infoY + 100);
        g.drawString("Best: " + board.getBestScore(), infoX + 20, infoY + 130);

        g.drawLine(infoX + 10, infoY + 160, infoX + width - 10, infoY + 160);
        g.drawString("Controls:", infoX + 20, infoY + 190);

        String[] controls = {
                "←↑↓→ / WASD - Move",
                "P - Pause/Resume",
                "R - Restart",
                "ESC - Exit"
        };

        for (int i = 0; i < controls.length; i++) {
            g.drawString(controls[i], infoX + 30, infoY + 220 + i * 25);
        }

        if (gameWon) {
            g.setColor(Color.GREEN);
            g.drawString("YOU WIN!", infoX + 20, infoY + 340);
        } else if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("GAME OVER", infoX + 20, infoY + 340);
        } else if (paused) {
            g.setColor(Color.ORANGE);
            g.drawString("PAUSED", infoX + 20, infoY + 340);
        }
    }

    private void drawGameStatus(Graphics2D g) {
        if (!paused && !gameWon && !gameOver) return;

        g.setColor(new Color(0, 0, 0, 150));
        int boardX = GameConstants.PANEL_PADDING;
        int boardY = GameConstants.PANEL_PADDING;
        g.fillRoundRect(boardX, boardY, GameConstants.GAME_PANEL_SIZE,
                GameConstants.GAME_PANEL_SIZE, 15, 15);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics fm = g.getFontMetrics();

        String statusText = "";
        String subText = "";
        Color color = Color.WHITE;

        if (gameWon) {
            statusText = "YOU WIN!";
            subText = "Press R to restart";
            color = Color.GREEN;
        } else if (gameOver) {
            statusText = "GAME OVER";
            subText = "Press R to restart";
            color = Color.RED;
        } else if (paused) {
            statusText = "PAUSED";
            subText = "Press P to continue";
            color = Color.ORANGE;
        }

        g.setColor(color);
        int centerX = boardX + GameConstants.GAME_PANEL_SIZE / 2;
        int centerY = boardY + GameConstants.GAME_PANEL_SIZE / 2;
        g.drawString(statusText, centerX - fm.stringWidth(statusText) / 2, centerY - 20);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        fm = g.getFontMetrics();
        g.drawString(subText, centerX - fm.stringWidth(subText) / 2, centerY + 20);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        repaint();
    }

    public void reset() {
        paused = false;
        gameOver = false;
        gameWon = false;
        repaint();
    }
}