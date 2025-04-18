package cn;

import core.Move;
import core.State;

import java.util.*;

public class CNState implements State<ConnectN> {
    public ConnectN theGame;
    public Position position;
    public Random random;
    public Optional<Integer> winner;

    public CNState(ConnectN theGame, Position position, Optional<Integer> winner) {
        this.theGame = theGame;
        this.position = position;
        this.random = theGame.random;
        this.winner = winner;
    }

    //initialize constructor
    public CNState(ConnectN theGame, int N, int width, int height) {
        this.theGame = theGame;
        this.position = new Position(N, width, height);
        this.random = theGame.random;
        this.winner = Optional.empty();
    }

    //initialize constructor
    public CNState(ConnectN theGame) {
        this.theGame = theGame;
        this.position = new Position();
        this.random = theGame.random;
        this.winner = Optional.empty();
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof CNState) {
            return position.equals(((CNState) obj).position);
        }
        return false;
    }
    @Override
    public State<ConnectN> next(Move<ConnectN> move) {
        Position newPosition = position.next(move);
        Optional<Integer> newWinner = newPosition.determineWinner();
        return new CNState(theGame, newPosition, newWinner);
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
