package test.cn;

import cn.*;
import core.Node;
import core.State;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class MCTSTest {

    @Test
    void chooseNext() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        assertTimeout(Duration.ofMillis(300), () -> mcts.chooseNext(1000));
    }

    @Test
    void getLastMove() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        assertEquals(-1, mcts.getLastMove());
        State<ConnectN> state = initState.next(new CNMove(initState.player(), 4));
        MCTS mcts1 = new MCTS(new CNNode(state));
        assertEquals(4, mcts1.getLastMove());
    }

    @Test
    void getCurrentPlayer() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        assertEquals('X', mcts.getCurrentPlayer());
        State<ConnectN> state = initState.next(new CNMove(initState.player(), 4));
        MCTS mcts1 = new MCTS(new CNNode(state));
        assertEquals('O', mcts1.getCurrentPlayer());
    }

    @Test
    void reportGameResult() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        assertThrows(RuntimeException.class, () -> mcts.reportGameResult());
        Node<ConnectN> leaf = mcts.rollout(initNode);
        MCTS mcts1 = new MCTS(leaf);
        mcts1.reportGameResult();
    }

    @Test
    void expand() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        assertEquals(0, mcts.root.children().size());
        mcts.chooseNext(30);
        mcts.expand();
        assertEquals(9, mcts.root.children().size());
    }

    @Test
    void select() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        mcts.expand();
        Node<ConnectN> node = mcts.select();
        assertEquals(0, ((CNState) node.state()).position.lastMove);
    }

    @Test
    void rollout() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        assertTimeout(Duration.ofMillis(300), () -> {
            for (int i = 0; i < 1000; i++) {
                mcts.rollout(initNode);
            }
        });
    }

    @Test
    void iteration() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        mcts.iteration(100);
        assertEquals(100, initNode.playouts());
        mcts.iteration(100);
        assertEquals(200, initNode.playouts());
    }
}