package connectfour;

import core.Node;
import java.util.List;
import java.util.Scanner;

import core.Move;

public class ConnectFourHumanVsMCTS {

    public static void main(String[] args) {
        ConnectFour game = new ConnectFour();
        ConnectFour.ConnectFourState state = (ConnectFour.ConnectFourState) game.start();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Start of Game. You are Player O (0). MCTS is Player X (1).");
        System.out.println(state);
        System.out.println("------");

        while (!state.isTerminal()) {
            if (state.player() == ConnectFour.PLAYER_TWO) {
                // Human turn
                List<Move<ConnectFour>> moves = (List<Move<ConnectFour>>) state.moves(state.player());
                System.out.println("Available columns:");
                for (int i = 0; i < moves.size(); i++) {
                    ConnectFour.ConnectFourMove m = (ConnectFour.ConnectFourMove) moves.get(i);
                    System.out.println(i + ": column " + m.col());
                }
                System.out.print("Enter move index: ");
                int choice = scanner.nextInt();
                state = (ConnectFour.ConnectFourState) state.next(moves.get(choice));
            } else {
                // MCTS turn
                ConnectFourNode root = new ConnectFourNode(state);
                MCTS mcts = new MCTS(root);
                mcts.runSearch(1000);
                Node<ConnectFour> best = mcts.selectBestChild(root);
                state = (ConnectFour.ConnectFourState) best.getState();
                System.out.println("MCTS plays:");
            }

            System.out.println(state);
            System.out.println("------");
        }

        if (state.winner().isPresent()) {
            int winner = state.winner().get();
            System.out.println("Game over! Winner: Player " + (winner == ConnectFour.PLAYER_ONE ? "X (1)" : "O (0)"));
        } else {
            System.out.println("Game over! It's a draw.");
        }
    }
}