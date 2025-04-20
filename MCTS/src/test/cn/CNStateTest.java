package test.cn;

import cn.CNState;
import cn.ConnectN;
import core.Move;
import core.State;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CNStateTest {

    @Test
    void game() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        assertSame(connectN, initState.game());
    }

    @Test
    void isTerminal() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        assertFalse(initState.isTerminal());
    }

    @Test
    void player() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        assertEquals(0, initState.player());
    }

    @Test
    void winner() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        assertEquals(Optional.empty(), initState.winner());
    }

    @Test
    void random() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        assertSame(connectN.random, initState.random());
    }

    @Test
    void moves() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Collection<Move<ConnectN>> initialMoves = initState.moves(initState.player());
        assertEquals(((CNState) initState).position.width, initialMoves.size());
    }

    @Test
    void next() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        //next is functional, so invoke next on possible moves
        //will not change the original state.
        for (Move<ConnectN> move : initState.moves(initState.player())) {
            initState.next(move);
        }
        State<ConnectN> state = connectN.start();
        assertEquals(initState, state);
    }
}