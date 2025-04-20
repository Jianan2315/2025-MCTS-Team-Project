package tictactoe;

import core.Node;
import core.State;
import core.Move;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {

    private final Node<TicTacToe> root;
    private final Random random = new Random();
    private static final int WIN_SCORE = 1;

    public MCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    public Node<TicTacToe> getRoot() {
        return root;
    }

    public static void main(String[] args) {
        MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        Node<TicTacToe> root = mcts.root;
        mcts.runSearch(1000); // Run MCTS for 1000 iterations
        Node<TicTacToe> best = mcts.selectBestChild(root);
        System.out.println("Best move selected: ");
        System.out.println(best.getState().toString());
    }

    public void runSearch(int iterations) {
        for (int i = 0; i < iterations; i++) {
            Node<TicTacToe> node = selection(root);
            Node<TicTacToe> expanded = expansion(node);
            int result = simulation(expanded);
            backpropagation(expanded, result);
        }
    }

    private Node<TicTacToe> selection(Node<TicTacToe> node) {
        while (!node.getState().isTerminal()) {
            if (node.hasUntriedMoves()) {
                return node;
            } else {
                node = node.selectChildUCT();
            }
        }
        return node;
    }

    private Node<TicTacToe> expansion(Node<TicTacToe> node) {
        if (!node.getState().isTerminal() && node.hasUntriedMoves()) {
            for (Move<TicTacToe> move : node.getState().moves(node.getState().player())) {
                if (move instanceof TicTacToe.TicTacToeMove tmove) {
                    if (tmove.row() == 1 && tmove.col() == 1) {
                        State<TicTacToe> nextState = node.getState().next(move);
                        Node<TicTacToe> centerChild = new TicTacToeNode(nextState);
                        node.children().add(centerChild);
                        return centerChild;
                    }
                }
            }
            return node.expand();  // fallback
        }
        return node;
    }

    private int simulation(Node<TicTacToe> node) {
        TicTacToe.TicTacToeState rolloutState = (TicTacToe.TicTacToeState) node.getState();
        rolloutState = new TicTacToe().new TicTacToeState(rolloutState.position()); // clone manually
        while (!rolloutState.isTerminal()) {
            List<Move<TicTacToe>> legalMoves = (List<Move<TicTacToe>>) rolloutState.moves(rolloutState.player());
            Move<TicTacToe> move = legalMoves.get(random.nextInt(legalMoves.size()));
            rolloutState = (TicTacToe.TicTacToeState) rolloutState.next(move);
        }
        return rolloutState.winner().map(winner -> winner == node.getState().player() ? WIN_SCORE : 0).orElse(0);
    }

    private void backpropagation(Node<TicTacToe> node, int result) {
        while (node != null) {
            node.update(result);
            node = node.getParent();
        }
    }

    public Node<TicTacToe> selectBestChild(Node<TicTacToe> node) {
        return Collections.max(node.children(), (a, b) -> Integer.compare(a.playouts(), b.playouts()));
    }
}