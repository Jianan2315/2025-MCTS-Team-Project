package connectfour;

import core.Node;
import core.State;
import core.Move;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class to represent a Monte Carlo Tree Search for ConnectFour.
 */
public class MCTS {

    private final Node<ConnectFour> root;
    private final Random random = new Random();
    private static final int WIN_SCORE = 1;

    public MCTS(Node<ConnectFour> root) {
        this.root = root;
    }

    public Node<ConnectFour> getRoot() {
        return root;
    }

    public static void main(String[] args) {
        MCTS mcts = new MCTS(new ConnectFourNode(new ConnectFour().new ConnectFourState()));
        Node<ConnectFour> root = mcts.root;
        mcts.runSearch(1000); // Run MCTS for 1000 iterations
        Node<ConnectFour> best = mcts.selectBestChild(root);
        System.out.println("Best move selected: ");
        System.out.println(best.getState().toString());
    }

    public void runSearch(int iterations) {
        for (int i = 0; i < iterations; i++) {
            Node<ConnectFour> node = selection(root);
            Node<ConnectFour> expanded = expansion(node);
            int result = simulation(expanded);
            backpropagation(expanded, result);
        }
    }

    private Node<ConnectFour> selection(Node<ConnectFour> node) {
        while (!node.getState().isTerminal()) {
            if (node.hasUntriedMoves()) {
                return node;
            } else {
                node = node.selectChildUCT();
            }
        }
        return node;
    }

    private Node<ConnectFour> expansion(Node<ConnectFour> node) {
        if (!node.getState().isTerminal() && node.hasUntriedMoves()) {
            for (Move<ConnectFour> move : node.getState().moves(node.getState().player())) {
                if (move instanceof ConnectFour.ConnectFourMove tmove) {
                    if (tmove.col() == 3) { // 假设我们想优先落子中间列（7列中下标 3 是中心）
                        State<ConnectFour> nextState = node.getState().next(move);
                        Node<ConnectFour> centerChild = new ConnectFourNode(nextState);
                        node.children().add(centerChild);
                        return centerChild;
                    }
                }
            }
            return node.expand();  // fallback
        }
        return node;
    }

    private int simulation(Node<ConnectFour> node) {
        ConnectFour.ConnectFourState rolloutState = (ConnectFour.ConnectFourState) node.getState();
        rolloutState = new ConnectFour().new ConnectFourState(rolloutState.board(), rolloutState.lastPlayer());
        while (!rolloutState.isTerminal()) {
            List<Move<ConnectFour>> legalMoves = (List<Move<ConnectFour>>) rolloutState.moves(rolloutState.player());
            Move<ConnectFour> move = legalMoves.get(random.nextInt(legalMoves.size()));
            rolloutState = (ConnectFour.ConnectFourState) rolloutState.next(move);
        }
        return rolloutState.winner().map(winner -> winner == node.getState().player() ? WIN_SCORE : 0).orElse(0);
    }

    private void backpropagation(Node<ConnectFour> node, int result) {
        while (node != null) {
            node.update(result);
            node = node.getParent();
        }
    }

    public Node<ConnectFour> selectBestChild(Node<ConnectFour> node) {
        return Collections.max(node.children(), (a, b) -> Integer.compare(a.playouts(), b.playouts()));
    }
}