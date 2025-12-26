package com.game2048;

import java.awt.Color;

public class GameConstants {
    public static final int BOARD_SIZE = 4;

    public static final int TILE_SIZE = 100;
    public static final int TILE_MARGIN = 10;
    public static final int PANEL_PADDING = 20;

    public static final int GAME_PANEL_SIZE = 
        BOARD_SIZE * TILE_SIZE + (BOARD_SIZE + 1) * TILE_MARGIN;
    public static final int INFO_PANEL_WIDTH = 250;
    public static final int WINDOW_WIDTH = 
        GAME_PANEL_SIZE + INFO_PANEL_WIDTH + PANEL_PADDING * 2;
    public static final int WINDOW_HEIGHT = 
        GAME_PANEL_SIZE + PANEL_PADDING * 2;

    public static final Color[] TILE_COLORS = {
        new Color(205, 193, 180),
        new Color(238, 228, 218),
        new Color(237, 224, 200),
        new Color(242, 177, 121),
        new Color(245, 149, 99),
        new Color(246, 124, 95),
        new Color(246, 94, 59),
        new Color(237, 207, 114),
        new Color(237, 204, 97),
        new Color(237, 200, 80),
        new Color(237, 197, 63),
        new Color(237, 194, 46)
    };

    public static final Color DARK_TEXT_COLOR = new Color(119, 110, 101);
    public static final Color LIGHT_TEXT_COLOR = new Color(249, 246, 242);

    public static final Color BACKGROUND_COLOR = new Color(250, 248, 239);
    public static final Color BOARD_COLOR = new Color(187, 173, 160);
    public static final Color INFO_PANEL_COLOR = new Color(187, 173, 160, 100);

    public static final int WINNING_VALUE = 2048;
    public static final int INITIAL_TILES_COUNT = 2;
}