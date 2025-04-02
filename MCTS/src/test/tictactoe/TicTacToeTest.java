package test.tictactoe;

import core.State;
import org.junit.Test;
import tictactoe.TicTacToe;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TicTacToeTest {
    /**
     *
     */
    @Test
    public void runGame() {
        long seed = 0L;
        TicTacToe target = new TicTacToe(seed); // games run here will all be deterministic.
        State<TicTacToe> state = target.runGame();
        Optional<Integer> winner = state.winner();
        if (winner.isPresent()) assertEquals(Integer.valueOf(TicTacToe.X), winner.get());
        else fail("no winner");
    }
}