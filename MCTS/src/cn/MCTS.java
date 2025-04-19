package cn;

import core.Move;
import core.Node;
import core.State;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

public class MCTS {
    public Node<ConnectN> root;

    public MCTS(Node<ConnectN> root) {
        this.root = root;
    }

    public static void main(String[] args) {
        ConnectN connectN = new ConnectN();
        State<ConnectN> initState = connectN.start();
        //State<ConnectN> initState = connectN.initializeState(5, 10, 10);
        Node<ConnectN> initNode = new CNNode(initState);
        MCTS mcts = new MCTS(initNode);
        int count = 0;
        int human = 0;
        int times = 1000;
        System.out.println(mcts.root.state());
        System.out.println();
        while (!mcts.root.isLeaf()) {
            if ((count % 2) == human) {
                mcts.humanNext();
                //mcts.chooseNextDecorated(times);
            } else {
                mcts.chooseNextDecorated(times);
            }
            count++;
        }
        mcts.reportGameResult();
    }

    public void humanNext() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your move (you are " + getCurrentPlayer());
        while (true) {
            int j = scanner.nextInt();
            if (j == -1) {
                if (((CNState) root.state()).position.count <= 1) {
                    System.out.println("You can't take back because you have not make a move!");
                } else {
                    System.out.println("You take back your last move!");
                    root = root.parent().parent();
                    System.out.println("And the current position is");
                    System.out.println(root.state());
                }
            } else if (!((CNState) root.state()).position.checkValidity(j)) {
                System.out.println("Invalid move!");
            } else {
                Move<ConnectN> move = new CNMove(root.state().player(), j);
                root = root.addChild(root.state().next(move));
                System.out.println(root.state());
                break;
            }
        }
        if (!root.isLeaf()) {
            iteration(1000);
            System.out.println("Estimated winning rate for human after move is " + root.winningRate());
            System.out.println();
        }
    }

    public void chooseNext(int n) {
        iteration(n);
        root = chooseBest();
    }

    public int getLastMove() {
        Position position = ((CNState) root.state()).position;
        return position.lastMove;
    }

    public char getCurrentPlayer() {
        return (root.state().player() == 0) ? 'X' : 'O';
    }

    public void chooseNextDecorated(int n) {
        System.out.println("AI thinking ... (AI is " + getCurrentPlayer());
        chooseNext(n);
        System.out.println("The chosen move is " + getLastMove());
        System.out.println("The resulting position is");
        System.out.println(root.state());
        System.out.println("Estimated winning rate after move for AI is " + root.winningRate());
        System.out.println();
    }

    public void reportGameResult() {
        if (!root.isLeaf()) throw new RuntimeException("the game is not over yet!");
        State<ConnectN> state = root.state();
        Optional<Integer> winner = state.winner();
        if (winner.isEmpty()) {
            System.out.println("draw");
        } else if (winner.get() == 0) {
            System.out.println("X wins");
        } else if (winner.get() == 1) {
            System.out.println("O wins");
        }
    }

    public void expand() {
        State<ConnectN> state = root.state();
        int player = state.player();
        Collection<Move<ConnectN>> moves = state.moves(player);
        for (Move<ConnectN> move : moves) {
            State<ConnectN> nextState = state.next(move);
            root.addChild(nextState);
        }
    }

    public double ucb1(Node<ConnectN> node) {
        int n = node.playouts();
        if (n == 0) {
            return Double.POSITIVE_INFINITY;
        }
        double w = node.wins();
        int N = node.parent().playouts();
        double C = Math.sqrt(2);
        return (w / n) + C * Math.sqrt(Math.log(N) / n);
    }

    public Node<ConnectN> select() {
        return root.children().stream()
                .max(Comparator.comparing(this::ucb1))
                .orElseThrow(() -> new RuntimeException("no children!"));
    }

    public Node<ConnectN> rollout(Node<ConnectN> node) {
        Node<ConnectN> n = node;
        while (!n.isLeaf()) {
            State<ConnectN> s = n.state();
            Move<ConnectN> m = s.chooseMove(s.player());
            State<ConnectN> suc = s.next(m);
            n = n.addChild(suc);
        }
        return n;
    }

    public void iteration(int n) {
        if (root.isLeaf()) throw new RuntimeException("a leaf node does not need mcts!");
        expand();
        for (int i = 0; i < n; i++) {
            Node<ConnectN> hopeful = select();
            Node<ConnectN> leaf = rollout(hopeful);
            leaf.backPropagate();
        }
    }

    public Node<ConnectN> chooseBest() {
        return root.children().stream()
                .max(Comparator.comparing(Node::playouts))
                .orElseThrow(() -> new RuntimeException("leaf node?"));
    }

}
