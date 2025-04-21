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

        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 0)); // P1
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 6)); // dummy

        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 1)); // P1
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 6)); // dummy

        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 2)); // P1
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 6)); // dummy

        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 3)); // P1

        assertTrue(state.winner().isPresent());
        assertEquals(1, (int) state.winner().get());
    }

    @Test
    public void testWinnerVertical() {
        ConnectFour game = new ConnectFour();
        ConnectFour.ConnectFourState state = (ConnectFour.ConnectFourState) game.start();
        int col = 3;
        for (int r = 0; r < 4; r++) {
            state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), col));
            state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), (col + 1) % 7)); // dummy
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

        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 0)); // P1
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 1)); // dummy

        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 1)); // P1
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 2)); // dummy

        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 2)); // P1
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 3)); // dummy

        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 2)); // P1 (second layer)
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 3)); // dummy

        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 3)); // P1
        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 4)); // dummy

        state = (ConnectFour.ConnectFourState) state.next(new ConnectFour.ConnectFourMove(state.player(), 3)); // P1 (third layer)

        assertTrue(state.winner().isPresent());
        assertEquals(1, (int) state.winner().get());
    }
}