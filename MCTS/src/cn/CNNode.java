package cn;

import core.Node;
import core.State;
import tictactoe.TicTacToe;
import tictactoe.TicTacToeNode;

import java.util.ArrayList;
import java.util.Collection;

public class CNNode implements Node<ConnectN> {
    public State<ConnectN> state;
    public Collection<Node<ConnectN>> children;
    public Node<ConnectN> parent;
    public double wins = 0;
    public int playouts = 0;

    public CNNode(State<ConnectN> state, Node<ConnectN> parent) {
        this.state = state;
        this.children = new ArrayList<>();
        this.parent = parent;
    }
    @Override
    public boolean isLeaf() {
        return state.isTerminal();
    }

    @Override
    public State<ConnectN> state() {
        return state;
    }

    @Override
    public boolean white() {
        return state.player() == state.game().opener();
    }

    @Override
    public Collection<Node<ConnectN>> children() {
        return children;
    }

    @Override
    public void backPropagate() {
        if (!isLeaf()) throw new RuntimeException("backpropagation should begin from leaf node!");
        playouts++;
        double val;
        if (state.winner().isEmpty()) {
            val = 0.5;
        } else {
            val = 1.0;
        }
        wins += val;
        Node<ConnectN> current = parent;
        while (current != null) {
            current.playoutsDelta(1);
            val = 1.0 - val;
            current.winsDelta(val);
            current = current.parent();
        }
    }

    @Override
    public Node<ConnectN> addChild(State<ConnectN> state) {
        for (Node<ConnectN> child : children) {
            if (child.state().equals(state)) return child;
        }
        Node<ConnectN> node = new CNNode(state, this);
        children.add(node);
        return node;
    }

    @Override
    public double wins() {
        return wins;
    }

    @Override
    public void winsDelta(double delta) {
        wins += delta;
    }

    @Override
    public int playouts() {
        return playouts;
    }

    @Override
    public void playoutsDelta(int delta) {
        playouts += delta;
    }

    @Override
    public Node<ConnectN> parent() {
        return parent;
    }
}
