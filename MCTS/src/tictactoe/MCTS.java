package tictactoe;

import core.Move;
import core.Node;
import core.State;

import java.util.*;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {

    public static void main(String[] args) {
        TicTacToe ticTacToe = new TicTacToe(42);
        Node<TicTacToe> root = new TicTacToeNode(ticTacToe.new TicTacToeState());
        Scanner scanner = new Scanner(System.in);
        int human = 1;
        int l = 0;
        int times = 10000;
        // This is where you process the MCTS to try to win the game.
        while (!root.isLeaf()) {
            System.out.println(root.state());
            if ((l % 2) == (1 - human)) {
                char P = (root.state().player() == 1) ? 'X' : 'O';
                System.out.println("enter your move (you are " + P);
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                Move<TicTacToe> humanMove = new TicTacToe.TicTacToeMove(root.state().player(), row, col);
                State<TicTacToe> s = root.state().next(humanMove);
                root = root.addChild(s);
            } else {
                System.out.println("AI thinking ...");
                MCTS mcts1 = new MCTS(root);
                mcts1.iteration(times);
                root = mcts1.chooseBest();
            }
            l++;
        }
        System.out.println(root.state());
        Optional<Integer> optionalInteger = root.state().winner();
        if (optionalInteger.isEmpty()) {
            System.out.println("draw");
        } else if (optionalInteger.get() == 1) {
            System.out.println("X wins");
        } else {
            System.out.println("O wins");
        }
    }
    public void expand() {
        State<TicTacToe> state = root.state();
        int player = state.player();
        Collection<Move<TicTacToe>> moves = state.moves(player);
        for (Move<TicTacToe> move : moves) {
            State<TicTacToe> nextState = state.next(move);
            root.addChild(nextState);
        }
    }
    public Node<TicTacToe> select() {
        Collection<Node<TicTacToe>> nodes = root.children();
        int randomIndex = root.state().random().nextInt(nodes.size());
        return new ArrayList<>(nodes).get(randomIndex);
    }
    public Node<TicTacToe> rollout(Node<TicTacToe> node) {
        Node<TicTacToe> n = node;
        while (!n.isLeaf()) {
            State<TicTacToe> s = n.state();
            Move<TicTacToe> m = s.chooseMove(s.player());
            State<TicTacToe> suc = s.next(m);
            n = n.addChild(suc);
        }
        return n;
    }
    public void iteration(int n) {
        //node should not be a leaf.
        if (root.isLeaf()) throw new RuntimeException("leaf node does not need mcts!");
        for (int i = 0; i < n; i++) {
            expand();
            Node<TicTacToe> hopeful = select();
            Node<TicTacToe> leaf = rollout(hopeful);
            leaf.backPropagate();
        }
    }
    public Node<TicTacToe> chooseBest() {
        return root.children().stream()
                .min(Comparator.comparing(node -> {
                    if (node.playouts() == 0) throw new RuntimeException("too few iterations");
                    return node.wins() / node.playouts();
                }))
                .orElseThrow(() -> new RuntimeException("leaf node?"));
    }

    public MCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    private final Node<TicTacToe> root;
}