package com.game2048;

public enum MoveDirection {
    UP(0),

    RIGHT(1),

    DOWN(2),

    LEFT(3);

    private final int code;

    MoveDirection(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MoveDirection fromKeyCode(int keyCode) {
        return switch (keyCode) {
            case 38 -> UP;
            case 39 -> RIGHT;
            case 40 -> DOWN;
            case 37 -> LEFT;

            case 87 -> UP;
            case 68 -> RIGHT;
            case 83 -> DOWN;
            case 65 -> LEFT;

            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case UP -> "UP";
            case RIGHT -> "RIGHT";
            case DOWN -> "DOWN";
            case LEFT -> "LEFT";
        };
    }
}