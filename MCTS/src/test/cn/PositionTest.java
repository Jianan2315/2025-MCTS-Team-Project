package test.cn;

import cn.CNMove;
import cn.Position;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void next() {
        Position position = new Position();
        Position position1 = position.next(new CNMove(position.currentPlayer, 0));
        assertEquals(-1, position.grid[6][0]);
        assertEquals(0, position1.grid[6][0]);
    }

    @Test
    void nextPlayer() {
        Position position = new Position();
        assertEquals(1, position.nextPlayer(0));
        assertEquals(0, position.nextPlayer(1));
        assertThrows(RuntimeException.class, () -> position.nextPlayer(-1));
    }

    @Test
    void render() {
        Position position = new Position();
        assertEquals('X', position.render(0));
        assertEquals('O', position.render(1));
        assertEquals(' ', position.render(-1));
    }

    @Test
    void copyGrid() {
        Position position = new Position();
        int[][] newGrid = position.copyGrid();
        assertTrue(Arrays.deepEquals(newGrid, position.grid));
        newGrid[0][0] = 0;
        assertFalse(Arrays.deepEquals(newGrid, position.grid));
    }

    @Test
    void full() {
        Position position = new Position();
        assertFalse(position.full());
    }

    @Test
    void colFull() {
        Position position = new Position();
        assertFalse(position.colFull(0));
        Position position1 = position.next(new CNMove(position.currentPlayer, 0));
        Position position2 = position1.next(new CNMove(position1.currentPlayer, 0));
        Position position3 = position2.next(new CNMove(position2.currentPlayer, 0));
        Position position4 = position3.next(new CNMove(position3.currentPlayer, 0));
        Position position5 = position4.next(new CNMove(position4.currentPlayer, 0));
        Position position6 = position5.next(new CNMove(position5.currentPlayer, 0));
        Position position7 = position6.next(new CNMove(position6.currentPlayer, 0));
        assertTrue(position7.colFull(0));
        //not a valid move!
        assertThrows(RuntimeException.class, () ->
                position7.next(new CNMove(position7.currentPlayer, 0)));
    }

    @Test
    void determineWinner() {
        Position position = new Position();
        assertEquals(Optional.empty(), position.determineWinner());
        Position position1 = position.next(new CNMove(0, 0))
                .next(new CNMove(1, 1))
                .next(new CNMove(0, 0))
                .next(new CNMove(1, 1))
                .next(new CNMove(0, 0))
                .next(new CNMove(1, 1))
                .next(new CNMove(0, 0))
                .next(new CNMove(1, 1))
                .next(new CNMove(0, 0));
        assertEquals(Optional.of(0), position1.determineWinner());
    }

    @Test
    void checkValidity() {
        Position position = new Position();
        assertFalse(position.checkValidity(-1));
        assertFalse(position.checkValidity(9));
        assertTrue(position.checkValidity(4));
    }
}