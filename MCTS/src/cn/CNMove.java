package cn;

import core.Move;

public class CNMove implements Move<ConnectN> {
    public int p;
    public int j;
    @Override
    public int player() {
        return p;
    }
    public CNMove(int p, int j) {
        this.p = p;
        this.j = j;
    }
}
