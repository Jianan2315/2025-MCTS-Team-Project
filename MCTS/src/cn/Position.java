package cn;

import core.Move;

import java.util.Arrays;
import java.util.Optional;

public class Position {
    public int N;
    public int width;
    public int height;
    public int[][] grid;
    //-1 means no player
    //0 means the first player
    //1 means the second player
    public int lastPlayer;
    public int currentPlayer;
    //-1 means no move
    //[0,N) means the move for each column
    public int lastMove;
    public int count;
    public int[] Rows;
    //initialize constructor
    public Position(int N, int width, int height) {
        this.N = N;
        this.width = width;
        this.height = height;
        this.grid = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = -1;
            }
        }
        this.lastPlayer = -1;
        this.currentPlayer = 0;
        this.lastMove = -1;
        this.count = 0;
        Rows = new int[width];
        for (int i = 0; i < width; i++) {
            Rows[i] = height;
        }
    }
    //initialize constructor
    public Position() {
        this.N = 5;
        this.width = 9;
        this.height = 7;
        this.grid = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = -1;
            }
        }
        this.lastPlayer = -1;
        this.currentPlayer = 0;
        this.lastMove = -1;
        this.count = 0;
        Rows = new int[width];
        for (int i = 0; i < width; i++) {
            Rows[i] = height;
        }
    }
    public Position(int N, int width, int height, int[][] grid, int lastPlayer, int currentPlayer, int lastMove, int count, int[] rows) {
        this.N = N;
        this.width = width;
        this.height = height;
        this.grid = grid;
        this.lastPlayer = lastPlayer;
        this.currentPlayer = currentPlayer;
        this.lastMove = lastMove;
        this.count = count;
        Rows = rows;
    }
    public Position next(Move<ConnectN> move) {
        if (move instanceof CNMove) {
            int p = move.player();
            if (p != currentPlayer) throw new RuntimeException("player mismatch!");
            int j = ((CNMove) move).j;
            int[][] newGrid = copyGrid();
            newGrid[Rows[j] - 1][j] = p;
            int[] newRows = Rows.clone();
            newRows[j] -= 1;
            return new Position(N, width, height, newGrid, p, nextPlayer(p), j, count + 1, newRows);
        } else {
            throw new RuntimeException("unknown move!");
        }
    }
    public int nextPlayer(int player) {
        return switch (player) {
            case 0 -> 1;
            case 1 -> 0;
            default -> throw new RuntimeException("unknown player!");
        };
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        return Arrays.deepEquals(grid, ((Position) obj).grid);
    }
    public char render(int x) {
        return switch (x) {
            case 0 -> 'X';
            case 1 -> 'O';
            default -> ' ';
        };
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            sb.append(i);
            sb.append(' ');
            for (int j = 0; j < width; j++) {
                sb.append("|");
                sb.append(render(grid[i][j]));
            }
            sb.append('|');
            sb.append('\n');
        }
        sb.append("  ");
        for (int i = 0; i < width; i++) {
            sb.append("+-");
        }
        sb.append("+\n  ");
        for (int i = 0; i < width; i++) {
            sb.append(' ');
            sb.append(i);
        }
        sb.append(' ');
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
        return Rows[j] == 0;
    }
    //determineWinner should only be invoked when count > 0
    public Optional<Integer> determineWinner() {
        if (count == 0) throw new RuntimeException("determineWinner should only be invoked when count > 0");
        if (count < 2 * N - 1) return Optional.empty();
        if (checkVertical()) return Optional.of(lastPlayer);
        if (checkHorizontal()) return Optional.of(lastPlayer);
        if (checkDiagonal()) return Optional.of(lastPlayer);
        return Optional.empty();
    }
    public boolean checkVertical() {
        int c = 0;
        for (int i = Rows[lastMove]; i < height; i++) {
            if (grid[i][lastMove] == lastPlayer) {
                c++;
            } else {
                break;
            }
        }
        return c == N;
    }
    public boolean checkHorizontal() {
        int c = 0;
        int row = Rows[lastMove];
        for (int j = lastMove; j >= 0; j--) {
            if (grid[row][j] == lastPlayer) {
                c++;
            } else {
                break;
            }
        }
        for (int j = lastMove + 1; j < width; j++) {
            if (grid[row][j] == lastPlayer) {
                c++;
            } else {
                break;
            }
        }
        return c >= N;
    }
    public boolean checkDiagonal() {
        int row = Rows[lastMove];
        int col = lastMove;
        int count1 = 0;
        int r1 = row;
        int c1 = col;
        while (r1 < height && c1 >= 0 && grid[r1][c1] == lastPlayer) {
            count1++;
            r1++;
            c1--;
        }
        r1 = row - 1;
        c1 = col + 1;
        while (r1 >= 0 && c1 < width && grid[r1][c1] == lastPlayer) {
            count1++;
            r1--;
            c1++;
        }
        if (count1 >= N) {
            return true;
        }
        int count2 = 0;
        int r2 = row;
        int c2 = col;
        while (r2 < height && c2 < width && grid[r2][c2] == lastPlayer) {
            count2++;
            r2++;
            c2++;
        }
        r2 = row - 1;
        c2 = col - 1;
        while (r2 >= 0 && c2 >= 0 && grid[r2][c2] == lastPlayer) {
            count2++;
            r2--;
            c2--;
        }
        return count2 >= N;
    }
}
