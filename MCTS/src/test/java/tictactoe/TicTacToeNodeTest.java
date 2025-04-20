package tictactoe;

import core.Node;
import core.State;
import org.junit.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class TicTacToeNodeTest {

    @Test
    public void winsAndPlayouts() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState(Position.parsePosition("X . 0\nX O .\nX . 0", TicTacToe.X));
        TicTacToeNode node = new TicTacToeNode(state);
        assertTrue(node.isLeaf());
        assertEquals(2, node.wins());
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
        assertEquals(0, node.children().size());
        node.expand(); // expand one child
        Collection<Node<TicTacToe>> children = node.children();
        assertEquals(1, children.size());
    }

    @Test
    public void addChild() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        State<TicTacToe> nextState = state.next(state.moves(state.player()).iterator().next());
        node.addChild(nextState);
        assertEquals(1, node.children().size());
    }

    @Test
    public void backPropagate() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        State<TicTacToe> nextState = state.next(state.moves(state.player()).iterator().next());
        node.addChild(nextState);
        Node<TicTacToe> child = node.children().iterator().next();
        child.update(2);
        node.backPropagate();
        assertEquals(2, node.wins());
        assertEquals(1, node.playouts());
    }
}