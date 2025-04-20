package core;

import java.util.Collection;
import java.util.Iterator;

public interface Node<G extends Game> {

    boolean isLeaf();

    State<G> state();

    boolean white();

    Collection<Node<G>> children();

    void addChild(State<G> state);

    void backPropagate();

    int wins();

    int playouts();

    // Added method declarations for MCTS compatibility
    State<G> getState();

    boolean hasUntriedMoves();

    Node<G> expand();

    Node<G> selectChildUCT();

    void update(int result);

    Node<G> getParent();
}