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
        //0 means the first player and X
        //1 means the second player and O
        int human = 0;
        int count = 0;
        int times = 5000;
        Scanner scanner = new Scanner(System.in);
        Node<ConnectN> currentNode = new CNNode(initState);
        while (!currentNode.isLeaf()) {
            System.out.println(currentNode.state());
            int currentPlayer = currentNode.state().player();
            char P = (currentPlayer == 0) ? 'X' : 'O';
            if ((count % 2) == human) {
//                System.out.println("enter your move (you are " + P);
//                int mov = scanner.nextInt();
//                Move<ConnectN> humanMove = new CNMove(currentPlayer, mov);
//                State<ConnectN> nextState = currentNode.state().next(humanMove);
//                currentNode = currentNode.addChild(nextState);
                System.out.println("AI thinking ... (AI is " + P);
                MCTS mcts = new MCTS(currentNode);
                mcts.iteration(times);
                currentNode = mcts.chooseBest();
                System.out.println("The move is " + ((CNState) currentNode.state()).position.lastMove);
                System.out.println("AI eval winning rate after move for " + P + ": " + currentNode.winningRate());
            } else {
                System.out.println("AI thinking ... (AI is " + P);
                MCTS mcts = new MCTS(currentNode);
                mcts.iteration(times);
                currentNode = mcts.chooseBest();
                System.out.println("The move is " + ((CNState) currentNode.state()).position.lastMove);
                System.out.println("AI eval winning rate after move for " + P + ": " + currentNode.winningRate());
            }
            count++;
        }
        State<ConnectN> currentState = currentNode.state();
        System.out.println(currentState);
        Optional<Integer> winner = currentState.winner();
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
