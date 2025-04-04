package tictactoe;

import core.Node;
import core.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class TicTacToeNode implements Node<TicTacToe> {

    /**
     * @return true if this node is a leaf node (in which case no further exploration is possible).
     */
    public boolean isLeaf() {
        return state().isTerminal();
    }

    /**
     * @return the State of the Game G that this Node represents.
     */
    public State<TicTacToe> state() {
        return state;
    }

    /**
     * Method to determine if the player who plays to this node is the opening player (by analogy with chess).
     * For this method, we assume that X goes first so is "white."
     * NOTE: this assumes a two-player game.
     *
     * @return true if this node represents a "white" move; false for "black."
     */
    public boolean white() {
        return state.player() == state.game().opener();
    }

    /**
     * @return the children of this Node.
     */
    public Collection<Node<TicTacToe>> children() {
        return children;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof TicTacToeNode) {
            return state.equals(((TicTacToeNode) obj).state);
        }
        return false;
    }

    /**
     * Method to add a child to this Node.
     *
     * @param state the State for the new chile.
     */
    public Node<TicTacToe> addChild(State<TicTacToe> state) {
        for (Node<TicTacToe> child : children) {
            if (child.state().equals(state)) return child;
        }
        Node<TicTacToe> node = new TicTacToeNode(state, this);
        children.add(node);
        return node;
    }

    /**
     * This method sets the number of wins and playouts according to the children states.
     */
    public void backPropagate() {
        if (children.isEmpty()) {
            playouts++;
            if (state.winner().isEmpty()) {
                wins += 0.5;
            }
        } else {
            playouts = 0;
            wins = 0;
            for (Node<TicTacToe> child : children) {
                wins += (child.playouts() - child.wins());
                playouts += child.playouts();
            }
        }
        if (parent != null) parent.backPropagate();
    }

    /**
     * @return the score for this Node and its descendents a win is worth 2 points, a draw is worth 1 point.
     */
    public double wins() {
        return wins;
    }

    /**
     * @return the number of playouts evaluated (including this node). A leaf node will have a playouts value of 1.
     */
    public int playouts() {
        return playouts;
    }

    public Node<TicTacToe> parent() {
        return parent;
    }

    public TicTacToeNode(State<TicTacToe> state) {
        this.state = state;
        children = new ArrayList<>();
        parent = null;
    }

    public TicTacToeNode(State<TicTacToe> state, Node<TicTacToe> parent) {
        this.state = state;
        children = new ArrayList<>();
        this.parent = parent;
    }

    private final State<TicTacToe> state;
    private final ArrayList<Node<TicTacToe>> children;
    private final Node<TicTacToe> parent;

    private double wins = 0;
    private int playouts = 0;
}