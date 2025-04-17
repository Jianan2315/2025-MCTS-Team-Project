package cn;

import core.Move;
import core.State;

import java.util.*;

public class CNState implements State<ConnectN> {
    public ConnectN theGame;
    public Position position;
    public Random random;
    public Optional<Integer> winner;

    public CNState(ConnectN theGame, Position position, Random random, Optional<Integer> winner) {
        this.theGame = theGame;
        this.position = position;
        this.random = random;
        this.winner = winner;
    }

    @Override
    public ConnectN game() {
        return theGame;
    }

    @Override
    public boolean isTerminal() {
        return (position.full() || winner.isPresent());
    }

    @Override
    public int player() {
        return position.currentPlayer;
    }

    @Override
    public Optional<Integer> winner() {
        return winner;
    }

    @Override
    public Random random() {
        return random;
    }

    @Override
    public Collection<Move<ConnectN>> moves(int player) {
        ArrayList<Move<ConnectN>> moves = new ArrayList<>();
        int p = position.currentPlayer;
        for (int j = 0; j < position.width; j++) {
            if (!position.colFull(j)) moves.add(new CNMove(p, j));
        }
        return moves;
    }

    @Override
    public State<ConnectN> next(Move<ConnectN> move) {
        return null;
    }
}
