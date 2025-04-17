package cn;

import core.Game;
import core.State;

public class ConnectN implements Game<ConnectN> {

    @Override
    public State<ConnectN> start() {
        return null;
    }

    @Override
    public int opener() {
        return 1;
    }
}
