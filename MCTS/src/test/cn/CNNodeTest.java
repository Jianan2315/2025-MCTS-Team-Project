package test.cn;

import cn.CNNode;
import cn.ConnectN;
import cn.MCTS;
import core.Node;
import core.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CNNodeTest {

    @Test
    void isLeaf() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        assertFalse(initNode.isLeaf());
        Node<ConnectN> leaf = mcts.rollout(initNode);
        assertTrue(leaf.isLeaf());
    }

    @Test
    void state() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        assertSame(initState, initNode.state());
    }

    @Test
    void white() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        assertTrue(initNode.white());
    }

    @Test
    void children() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        assertEquals(0, initNode.children().size());
        mcts.expand();
        assertEquals(9, initNode.children().size());
    }

    @Test
    void backPropagate() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        mcts.iteration(100);
        assertEquals(100, initNode.playouts());
        assertTrue(initNode.wins() <= 100 && initNode.wins() >= 0);
    }

    @Test
    void addChild() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        State<ConnectN> state = initState.next(initState.chooseMove(0));
        assertEquals(0, initNode.children().size());
        initNode.addChild(state);
        assertEquals(1, initNode.children().size());
        initNode.addChild(state);
        assertEquals(1, initNode.children().size());
    }

    @Test
    void wins() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        mcts.iteration(100);
        assertTrue(initNode.wins() <= 100 && initNode.wins() >= 0);
    }

    @Test
    void winsDelta() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        initNode.winsDelta(1.5);
        assertEquals(1.5, initNode.wins());
        initNode.winsDelta(2.5);
        assertEquals(4.0, initNode.wins());
    }

    @Test
    void playouts() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        mcts.iteration(100);
        assertEquals(100, initNode.playouts());
    }

    @Test
    void playoutsDelta() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        initNode.playoutsDelta(10);
        assertEquals(10, initNode.playouts());
        initNode.playoutsDelta(23);
        assertEquals(33, initNode.playouts());
    }

    @Test
    void parent() {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        Node<ConnectN> initNode = new CNNode(initState);
        assertNull(initNode.parent());
        MCTS mcts = new MCTS(initNode);
        mcts.chooseNext(30);
        assertSame(initNode, mcts.root.parent());
    }
}