package cn;

import core.Node;
import core.State;

import java.util.Collection;
import java.util.List;

public class CNNode implements Node<ConnectN> {
    public State<ConnectN> state;
    public Collection<Node<ConnectN>> children;
    public Node<ConnectN> parent;
    public double wins = 0;
    public int playouts = 0;

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

    }

    @Override
    public Node<ConnectN> addChild(State<ConnectN> state) {
        return null;
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
