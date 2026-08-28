package com.pvz2.network.onlineIZombie.messages;

public class MatchOver {
    public String matchId;
    public String winnerSide; // "PLANTS" or "ZOMBIES"
    public String message;

    public MatchOver() {}
    public MatchOver(String matchId, String winnerSide, String message) {
        this.matchId = matchId;
        this.winnerSide = winnerSide;
        this.message = message;
    }
}
