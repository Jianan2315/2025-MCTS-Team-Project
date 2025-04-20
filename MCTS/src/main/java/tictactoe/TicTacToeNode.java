package tictactoe;

import core.Node;
import core.State;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TicTacToeNode implements Node<TicTacToe> {

    private final State<TicTacToe> state;
    private final ArrayList<Node<TicTacToe>> children;
    private final List<core.Move<TicTacToe>> untriedMoves;
    private final Node<TicTacToe> parent;
    private int wins;
    private int playouts;

    private static final double EXPLORATION_CONSTANT = Math.sqrt(2);
    private static final Random random = new Random();

    // Root node constructor
    public TicTacToeNode(State<TicTacToe> state) {
        this(state, null);
    }

    // General constructor
    public TicTacToeNode(State<TicTacToe> state, Node<TicTacToe> parent) {
        this.state = state;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.untriedMoves = new ArrayList<>(state.moves(state.player()));
        initializeNodeData();
    }

    private void initializeNodeData() {
        if (isLeaf()) {
            playouts = 1;
            Optional<Integer> winner = state.winner();
            if (winner.isPresent()) {
                wins = 2;
            } else {
                wins = 1;
            }
        }
    }

    public boolean isLeaf() {
        return state().isTerminal();
    }

    public State<TicTacToe> state() {
        return state;
    }

    public boolean white() {
        return state.player() == state.game().opener();
    }

    public Collection<Node<TicTacToe>> children() {
        return children;
    }

    public void addChild(State<TicTacToe> state) {
        children.add(new TicTacToeNode(state, this));
    }

    public void backPropagate() {
        playouts = 0;
        wins = 0;
        for (Node<TicTacToe> child : children) {
            wins += child.wins();
            playouts += child.playouts();
        }
    }

    public int wins() {
        return wins;
    }

    public int playouts() {
        return playouts;
    }

    public boolean hasUntriedMoves() {
        return !untriedMoves.isEmpty();
    }

    public Node<TicTacToe> expand() {
        core.Move<TicTacToe> move = untriedMoves.remove(random.nextInt(untriedMoves.size()));
        State<TicTacToe> nextState = state.next(move);
        TicTacToeNode childNode = new TicTacToeNode(nextState, this);
        children.add(childNode);
        return childNode;
    }

    public Node<TicTacToe> getParent() {
        return parent;
    }

    public State<TicTacToe> getState() {
        return state;
    }

    public void update(int result) {
        playouts++;
        wins += result;
    }

    public Node<TicTacToe> selectChildUCT() {
        return children.stream().max((a, b) -> Double.compare(
                uctValue((TicTacToeNode) a),
                uctValue((TicTacToeNode) b)
        )).orElseThrow();
    }

    private double uctValue(TicTacToeNode node) {
        if (node.playouts == 0) return Double.POSITIVE_INFINITY;
        double exploitation = (double) node.wins / node.playouts;
        double exploration = EXPLORATION_CONSTANT * Math.sqrt(Math.log(this.playouts + 1) / node.playouts);
        return exploitation + exploration;
    }
}