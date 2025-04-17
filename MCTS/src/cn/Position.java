package cn;

import java.util.Arrays;

public class Position {
    public int N;
    public int width;
    public int height;
    public int[][] grid;
    //0 means no player
    //1 means the first player
    //2 means the second player
    public int lastPlayer;
    public int currentPlayer;
    public int count;
    public Position(int N, int width, int height) {
        this.N = N;
        this.width = width;
        this.height = height;
        this.grid = new int[height][width];
        this.lastPlayer = 0;
        this.currentPlayer = 1;
        this.count = 0;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        return Arrays.deepEquals(grid, ((Position) obj).grid);
    }
    public char render(int x) {
        return switch (x) {
            case 1 -> 'X';
            case 2 -> 'O';
            default -> '_';
        };
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(render(grid[i][j]));
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    public int[][] copyGrid() {
        int[][] result = new int[height][];
        for (int i = 0; i < height; i++) {
            result[i] = grid[i].clone();
        }
        return result;
    }
    public boolean full() {
        return (count == width * height);
    }
    public boolean colFull(int j) {
        return switch (grid[0][j]) {
            case 1, 2 -> true;
            default -> false;
        };
    }

}
