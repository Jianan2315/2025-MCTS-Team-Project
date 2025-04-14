package test.tictactoe;

import core.Node;
import core.State;
import tictactoe.MCTS;
import tictactoe.Position;
import tictactoe.TicTacToe;
import tictactoe.TicTacToeNode;

import org.junit.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


public class MCTSTest {
    @Test
    public void expand() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        Node<TicTacToe> node = new TicTacToeNode(state);
        MCTS mcts = new MCTS(node);
        mcts.expand();
        assertEquals(9, node.children().size());
        Node<TicTacToe> node1 = node.children().iterator().next();
        MCTS mcts1 = new MCTS(node1);
        mcts1.expand();
        assertEquals(8, node1.children().size());
        Node<TicTacToe> node2 = node1.children().iterator().next();
        MCTS mcts2 = new MCTS(node2);
        mcts2.expand();
        assertEquals(7, node2.children().size());
    }

    @Test
    public void select() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        Node<TicTacToe> node = new TicTacToeNode(state);
        MCTS mcts = new MCTS(node);
        assertThrows(RuntimeException.class, mcts::select);
        mcts.expand();
        Node<TicTacToe> node1 = node.children().iterator().next();
        assertSame(node1, mcts.select());
    }

    @Test
    public void rollout() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        Node<TicTacToe> node = new TicTacToeNode(state);
        MCTS mcts = new MCTS(node);
        mcts.expand();
        for (int i = 0; i < 100; i++) {
            Node<TicTacToe> hopeful = mcts.select();
            Node<TicTacToe> leaf = mcts.rollout(hopeful);
            assertTrue(leaf.isLeaf());
            leaf.backPropagate();
        }
        assertEquals(100, node.playouts());
    }

    @Test
    public void iteration() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        Node<TicTacToe> node = new TicTacToeNode(state);
        MCTS mcts = new MCTS(node);
        //maybe fail due to variance of performance
        assertTimeout(Duration.ofMillis(500), () -> mcts.iteration(10000));
    }

    @Test
    public void chooseBest() {
        TicTacToe ticTacToe = new TicTacToe();
        TicTacToe.TicTacToeState state = ticTacToe.new TicTacToeState();
        Node<TicTacToe> node = new TicTacToeNode(state);
        MCTS mcts = new MCTS(node);
        mcts.iteration(2000);
        Node<TicTacToe> node1 = mcts.chooseBest();
        assertEquals(ticTacToe.new TicTacToeState(Position.parsePosition(". . .\n. X .\n. . .", 1)), node1.state());
    }

    @Test
    public void ucb1() {
        TicTacToe ticTacToe = new TicTacToe();
        TicTacToe.TicTacToeState state = ticTacToe.new TicTacToeState();
        Node<TicTacToe> node = new TicTacToeNode(state);
        MCTS mcts = new MCTS(node);
        mcts.expand();
        Node<TicTacToe> node1 = mcts.select();
        assertEquals(Double.POSITIVE_INFINITY, mcts.ucb1(node1));
        State<TicTacToe> state1 = ticTacToe.new TicTacToeState(Position.parsePosition(". . .\n. X .\n. . .", 1));
        Node<TicTacToe> child = node.addChild(state1);
        mcts.iteration(10000);
        assertTrue(node1.playouts() < child.playouts());
    }

}