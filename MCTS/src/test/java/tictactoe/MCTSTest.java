package tictactoe;

import core.Node;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MCTSTest {

    @Test
    public void testMCTSRunAndSelectBest() {
        TicTacToe.TicTacToeState initialState = new TicTacToe().new TicTacToeState();
        Node<TicTacToe> root = new TicTacToeNode(initialState);
        MCTS mcts = new MCTS(root);

        mcts.runSearch(100); // run with limited iterations for speed

        Node<TicTacToe> bestChild = getBestChild(root);
        assertNotNull(bestChild, "Best child after MCTS should not be null");

        List<Node<TicTacToe>> children = (List<Node<TicTacToe>>) root.children();
        boolean foundVisited = false;
        for (Node<TicTacToe> child : children) {
            if (child.playouts() > 0) {
                foundVisited = true;
                break;
            }
        }

        assertTrue(foundVisited, "At least one child should have been visited");

        System.out.println("Best move's resulting state:");
        System.out.println(bestChild.getState().toString());
    }

    private Node<TicTacToe> getBestChild(Node<TicTacToe> node) {
        return node.children().stream()
                .max((a, b) -> Integer.compare(a.playouts(), b.playouts()))
                .orElse(null);
    }
}