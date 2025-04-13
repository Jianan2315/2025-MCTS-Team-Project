package test.tictactoe;

import core.Move;
import core.Node;
import core.State;
import org.junit.Test;
import tictactoe.MCTS;
import tictactoe.Position;
import tictactoe.TicTacToe;
import tictactoe.TicTacToeNode;

import static org.junit.jupiter.api.Assertions.*;

public class TicTacToeNodeTest {

    @Test
    public void winsAndPlayouts() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState(Position.parsePosition("X . 0\nX O .\nX . 0", TicTacToe.X));
        TicTacToeNode node = new TicTacToeNode(state);
        node.backPropagate();
        assertTrue(node.isLeaf());
        assertEquals(1.0, node.wins());
        assertEquals(1, node.playouts());
    }

    @Test
    public void state() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        assertEquals(state, node.state());
    }

    @Test
    public void white() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        assertTrue(node.white());
    }

    @Test
    public void children() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        assertTrue(node.children().isEmpty());
        Move<TicTacToe> move = state.chooseMove(state.player());
        State<TicTacToe> newState = state.next(move);
        node.addChild(newState);
        assertFalse(node.children().isEmpty());
    }

    @Test
    public void addChild() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        Node<TicTacToe> node = new TicTacToeNode(state);
        Move<TicTacToe> move = state.chooseMove(state.player());
        State<TicTacToe> newState = state.next(move);
        node.addChild(newState);
        Node<TicTacToe> childNode1 = node.addChild(newState);
        Node<TicTacToe> childNode2 = node.addChild(newState);
        assertSame(childNode1, childNode2);
        assertEquals(1, node.children().size());
    }

    @Test
    public void backPropagate() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        Node<TicTacToe> node = new TicTacToeNode(state);
        MCTS mcts = new MCTS(node);
        mcts.iteration(100);
        assertEquals(100, node.playouts());
        assertTrue(0.0 <= node.wins() && node.wins() <= 100.0);
    }

    @Test
    public void playoutsDelta() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        Node<TicTacToe> node = new TicTacToeNode(state);
        node.playoutsDelta(3);
        assertEquals(3, node.playouts());
        node.playoutsDelta(-1);
        assertEquals(2, node.playouts());
        node.playoutsDelta(-2);
        assertEquals(0, node.playouts());
    }

    @Test
    public void winsDelta() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        Node<TicTacToe> node = new TicTacToeNode(state);
        node.winsDelta(10.56);
        assertEquals(10.56, node.wins());
        node.winsDelta(5.3);
        assertEquals(15.86, node.wins());
    }
}