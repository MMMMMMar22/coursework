package com.game2048;

import java.awt.Color;

public class Tile {
    private int value;
    private int row;
    private int col;
    private boolean merged;

    public Tile(int value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.merged = false;
    }

    public static Tile createEmpty(int row, int col) {
        return new Tile(0, row, col);
    }

    public static Tile createWithValue(int value, int row, int col) {
        return new Tile(value, row, col);
    }

    public static Tile copyFrom(Tile other) {
        Tile tile = new Tile(other.value, other.row, other.col);
        tile.merged = other.merged;
        return tile;
    }

    public void doubleValue() {
        this.value *= 2;
        this.merged = true;
    }

    public void resetMergeFlag() {
        this.merged = false;
    }

    public boolean canMergeWith(Tile other) {
        return other != null &&
                this.value == other.value &&
                !this.merged && !other.merged;
    }

    public Color getColor() {
        if (value == 0) return GameConstants.TILE_COLORS[0];

        int index = (int) (Math.log(value) / Math.log(2));
        if (index >= GameConstants.TILE_COLORS.length) {
            index = GameConstants.TILE_COLORS.length - 1;
        }
        return GameConstants.TILE_COLORS[index];
    }

    public Color getTextColor() {
        return value < 8 ? GameConstants.DARK_TEXT_COLOR : GameConstants.LIGHT_TEXT_COLOR;
    }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }
    public boolean isMerged() { return merged; }
    public void setMerged(boolean merged) { this.merged = merged; }
    public boolean isEmpty() { return value == 0; }
}