
package connectfour;

import core.Game;
import core.Move;
import core.State;

import java.util.*;

/**
 * Connect Four game implementation (7 columns × 6 rows, drop-down gravity).
 */
public class ConnectFour implements Game<ConnectFour> {

    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 0;
    public static final int EMPTY = -1;

    public static class ConnectFourMove implements Move<ConnectFour> {
        private final int player;
        private final int col;

        public ConnectFourMove(int player, int col) {
            this.player = player;
            this.col = col;
        }

        public int player() {
            return player;
        }

        public int col() {
            return col;
        }
    }

    public class ConnectFourState implements State<ConnectFour> {
        private final int[][] board;
        private final int lastPlayer;
        private final Random rand = random;

        public ConnectFourState() {
            this.board = new int[ROWS][COLS];
            for (int[] row : board) Arrays.fill(row, EMPTY);
            this.lastPlayer = PLAYER_TWO;
        }

        public ConnectFourState(int[][] board, int lastPlayer) {
            this.board = board;
            this.lastPlayer = lastPlayer;
        }

        public ConnectFour game() {
            return ConnectFour.this;
        }

        public int player() {
            return 1 - lastPlayer;
        }

        public Random random() {
            return rand;
        }

        public boolean isTerminal() {
            return winner().isPresent() || full();
        }

        public Optional<Integer> winner() {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    int p = board[r][c];
                    if (p == EMPTY) continue;
                    if (c + 3 < COLS &&
                        p == board[r][c+1] && p == board[r][c+2] && p == board[r][c+3])
                        return Optional.of(p);
                    if (r + 3 < ROWS &&
                        p == board[r+1][c] && p == board[r+2][c] && p == board[r+3][c])
                        return Optional.of(p);
                    if (r + 3 < ROWS && c + 3 < COLS &&
                        p == board[r+1][c+1] && p == board[r+2][c+2] && p == board[r+3][c+3])
                        return Optional.of(p);
                    if (r + 3 < ROWS && c - 3 >= 0 &&
                        p == board[r+1][c-1] && p == board[r+2][c-2] && p == board[r+3][c-3])
                        return Optional.of(p);
                }
            }
            return Optional.empty();
        }

        public boolean full() {
            for (int c = 0; c < COLS; c++) {
                if (board[0][c] == EMPTY) return false;
            }
            return true;
        }

        public Collection<Move<ConnectFour>> moves(int player) {
            if (player == lastPlayer) throw new RuntimeException("Same player move");
            List<Move<ConnectFour>> legal = new ArrayList<>();
            for (int c = 0; c < COLS; c++) {
                if (board[0][c] == EMPTY)
                    legal.add(new ConnectFourMove(player, c));
            }
            return legal;
        }

        public State<ConnectFour> next(Move<ConnectFour> move) {
            ConnectFourMove m = (ConnectFourMove) move;
            int[][] newBoard = deepCopy(board);
            for (int r = ROWS - 1; r >= 0; r--) {
                if (newBoard[r][m.col] == EMPTY) {
                    newBoard[r][m.col] = m.player;
                    break;
                }
            }
            return new ConnectFourState(newBoard, m.player);
        }

        private int[][] deepCopy(int[][] src) {
            int[][] copy = new int[ROWS][COLS];
            for (int i = 0; i < ROWS; i++)
                copy[i] = Arrays.copyOf(src[i], COLS);
            return copy;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("ConnectFour{\n");
            for (int[] row : board) {
                for (int cell : row) sb.append(cell).append(" ");
                sb.append("\n");
            }
            sb.append("}");
            return sb.toString();
        }

        public int[][] board() {
            return board;
        }

        public int lastPlayer() {
            return lastPlayer;
        }
    }

    private final Random random;

    public ConnectFour() {
        this(System.currentTimeMillis());
    }

    public ConnectFour(long seed) {
        this(new Random(seed));
    }

    public ConnectFour(Random random) {
        this.random = random;
    }

    public State<ConnectFour> start() {
        return new ConnectFourState();
    }

    public int opener() {
        return PLAYER_ONE;
    }
}
