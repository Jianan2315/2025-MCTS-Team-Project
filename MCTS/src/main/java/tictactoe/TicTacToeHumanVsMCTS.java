package tictactoe;

import core.Node;
import core.Move;

import java.util.List;
import java.util.Scanner;

import tictactoe.TicTacToe.TicTacToeMove;

public class TicTacToeHumanVsMCTS {

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        TicTacToe.TicTacToeState state = game.new TicTacToeState();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Start of Game. You are Player O (0). MCTS is Player X (1).");
        System.out.println(state);

        while (!state.isTerminal()) {
            if (state.player() == TicTacToe.O) {
                // Human turn
                List<Move<TicTacToe>> legalMoves = (List<Move<TicTacToe>>) state.moves(state.player());
                System.out.println("Available moves (row col):");
                for (int i = 0; i < legalMoves.size(); i++) {
                    TicTacToeMove move = (TicTacToeMove) legalMoves.get(i);
                    System.out.println(i + ": (" + move.row() + ", " + move.col() + ")");
                }

                int choice = -1;
                while (choice < 0 || choice >= legalMoves.size()) {
                    System.out.print("Enter move index: ");
                    choice = scanner.nextInt();
                }

                state = (TicTacToe.TicTacToeState) state.next(legalMoves.get(choice));
            } else {
                // MCTS turn
                MCTS mcts = new MCTS(new TicTacToeNode(state));
                mcts.runSearch(500);  // MCTS plays
                Node<TicTacToe> best = mcts.selectBestChild(mcts.getRoot());
                state = (TicTacToe.TicTacToeState) best.getState();
                System.out.println("MCTS plays:");
            }

            System.out.println(state);
            System.out.println("------");
        }

        if (state.winner().isPresent()) {
            int winner = state.winner().get();
            System.out.println("Game Over. Winner is: " + (winner == TicTacToe.X ? "MCTS (X)" : "You (O)"));
        } else {
            System.out.println("Game Over. It's a draw.");
        }

        scanner.close();
    }
}