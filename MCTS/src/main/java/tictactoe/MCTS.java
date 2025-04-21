package tictactoe;

import core.Node;
import core.State;
import core.Move;

import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        Node<TicTacToe> root = mcts.getRoot();
        mcts.runSearch(1000);
        Node<TicTacToe> best = mcts.selectBestChild(root);
        System.out.println("Best move selected:");
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
            return node.expand();
        }
        return node;
    }

    // ✅ Lookahead-based defense logic
    private int simulation(Node<TicTacToe> node) {
        TicTacToe.TicTacToeState state = (TicTacToe.TicTacToeState) node.getState();
        state = new TicTacToe().new TicTacToeState(state.position());
        int currentPlayer = state.player();

        while (!state.isTerminal()) {
            List<Move<TicTacToe>> legalMoves = (List<Move<TicTacToe>>) state.moves(state.player());
            Move<TicTacToe> chosen = null;

            // 1. Try to win immediately
            for (Move<TicTacToe> move : legalMoves) {
                TicTacToe.TicTacToeState next = (TicTacToe.TicTacToeState) state.next(move);
                if (next.winner().isPresent() && next.winner().get() == state.player()) {
                    chosen = move;
                    break;
                }
            }

            // 2. Block opponent from winning in next turn
            if (chosen == null) {
                int opponent = 1 - state.player();
                for (Move<TicTacToe> move : legalMoves) {
                    TicTacToe.TicTacToeState next = (TicTacToe.TicTacToeState) state.next(move);
                    List<Move<TicTacToe>> oppMoves = (List<Move<TicTacToe>>) next.moves(opponent);
                    for (Move<TicTacToe> oppMove : oppMoves) {
                        TicTacToe.TicTacToeState future = (TicTacToe.TicTacToeState) next.next(oppMove);
                        if (future.winner().isPresent() && future.winner().get() == opponent) {
                            chosen = move;
                            break;
                        }
                    }
                    if (chosen != null) break;
                }
            }

            // 3. Random fallback
            if (chosen == null) {
                chosen = legalMoves.get(random.nextInt(legalMoves.size()));
            }

            state = (TicTacToe.TicTacToeState) state.next(chosen);
        }

        return state.winner().map(winner -> winner == currentPlayer ? WIN_SCORE : 0).orElse(0);
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
