
package connectfour;

import core.Node;
import core.State;
import core.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConnectFourNode implements Node<ConnectFour> {

    private final State<ConnectFour> state;
    private final Node<ConnectFour> parent;
    private final List<Node<ConnectFour>> children = new ArrayList<>();
    private final List<Move<ConnectFour>> untriedMoves;
    private final Move<ConnectFour> move;
    private int wins = 0;
    private int playouts = 0;

    public ConnectFourNode(State<ConnectFour> state) {
        this(state, null, null);
    }

    public ConnectFourNode(State<ConnectFour> state, Node<ConnectFour> parent, Move<ConnectFour> move) {
        this.state = state;
        this.parent = parent;
        this.move = move;
        this.untriedMoves = new ArrayList<>(state.moves(state.player()));
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
        return state.isTerminal();
    }

    public State<ConnectFour> state() {
        return state;
    }

    public boolean white() {
        return state.player() == state.game().opener();
    }

    public State<ConnectFour> getState() {
        return state;
    }

    public Node<ConnectFour> getParent() {
        return parent;
    }

    public boolean hasUntriedMoves() {
        return !untriedMoves.isEmpty();
    }

    public Node<ConnectFour> expand() {
        Move<ConnectFour> move = untriedMoves.remove(0);
        State<ConnectFour> nextState = state.next(move);
        ConnectFourNode child = new ConnectFourNode(nextState, this, move);
        children.add(child);
        return child;
    }

    public List<Node<ConnectFour>> children() {
        return children;
    }

    public void addChild(State<ConnectFour> state) {
        children.add(new ConnectFourNode(state, this, null));
    }

    public void backPropagate() {
        playouts = 0;
        wins = 0;
        for (Node<ConnectFour> child : children) {
            wins += child.wins();
            playouts += child.playouts();
        }
    }


    public Move<ConnectFour> getMove() {
        return move;
    }

    public void update(int result) {
        playouts++;
        wins += result;
    }

    public int wins() {
        return wins;
    }

    public int playouts() {
        return playouts;
    }

    public Node<ConnectFour> selectChildUCT() {
        return children.stream().max((a, b) -> Double.compare(
            uctValue((ConnectFourNode) a),
            uctValue((ConnectFourNode) b)
        )).orElseThrow();
    }

    private double uctValue(ConnectFourNode node) {
        if (node.playouts == 0) return Double.POSITIVE_INFINITY;
        double exploitation = (double) node.wins / node.playouts;
        double exploration = Math.sqrt(Math.log(this.playouts + 1) / node.playouts);
        return exploitation + exploration;
    }
}
