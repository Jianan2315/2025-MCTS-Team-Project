package cn;

import core.Game;
import core.State;

import java.util.Random;

public class ConnectN implements Game<ConnectN> {
    public Random random;

    @Override
    public State<ConnectN> start() {
        return new CNState(this);
    }

    @Override
    public int opener() {
        return 0;
    }

    public ConnectN() {
        this.random = new Random();
    }

    public ConnectN(long seed) {
        this.random = new Random(seed);
    }

    public State<ConnectN> initializeState(int N, int width, int height) {
        return new CNState(this, N, width, height);
    }


}
