package connectfour;

import core.Move;
import core.State;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class ConnectFourTest {

    @Test
    public void testInitialStateHasAllColumnsAvailable() {
        ConnectFour game = new ConnectFour();
        State<ConnectFour> state = game.start();
        List<Move<ConnectFour>> moves = (List<Move<ConnectFour>>) state.moves(state.player());
        assertEquals(7, moves.size());
    }

    @Test
    public void testApplyMoveAndSwitchPlayer() {
        ConnectFour game = new ConnectFour();
        State<ConnectFour> state = game.start();
        Move<ConnectFour> move = state.moves(state.player()).iterator().next();
        State<ConnectFour> next = state.next(move);
        assertNotEquals(state.player(), next.player());
    }

    @Test
    public void testWinnerHorizontal() {
        ConnectFour game = new ConnectFour();
        ConnectFour.ConnectFourState state = (ConnectFour.ConnectFourState) game.start();
        for (int c = 0; c < 4; c++) {
            state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(1, c)); // Player 1
            state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(0, c)); // Player 0 dummy
        }
        assertTrue(state.winner().isPresent());
        assertEquals(1, (int) state.winner().get());
    }

    @Test
    public void testWinnerVertical() {
        ConnectFour game = new ConnectFour();
        ConnectFour.ConnectFourState state = (ConnectFour.ConnectFourState) game.start();
        int col = 3;
        for (int r = 0; r < 4; r++) {
            state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(1, col)); // Player 1
            state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(0, col == 3 ? 2 : 3)); // Avoid block
        }
        assertTrue(state.winner().isPresent());
        assertEquals(1, (int) state.winner().get());
    }

    @Test
    public void testToStringOutput() {
        ConnectFour game = new ConnectFour();
        State<ConnectFour> state = game.start();
        assertTrue(state.toString().contains("ConnectFour"));
    }

    @Test
    public void testWinnerDiagonal() {
        ConnectFour game = new ConnectFour();
        ConnectFour.ConnectFourState state = (ConnectFour.ConnectFourState) game.start();

        // Diagonal win pattern for player 1 (\ direction)
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(1, 0)); // Row 5, Col 0
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(0, 1));
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(1, 1)); // Row 5, Col 1
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(0, 2));
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(1, 2));
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(0, 3));
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(1, 2)); // Row 4, Col 2
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(0, 3));
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(1, 3)); // Row 4, Col 3
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(0, 3));
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(1, 3)); // Row 3, Col 3

        assertTrue(state.winner().isPresent());
        assertEquals(1, (int) state.winner().get());
    }
}
