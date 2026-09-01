package com.pvz2.models.network.onlineIZombie.messages;

/** Pushed (no requestId) to BOTH players the instant a Match is created, whichever flow created it. */
public class MatchFound {
    public String matchId;
    public String opponentUsername;
    public String side; // "PLANTS" or "ZOMBIES" — this client's assigned role for the match

    public MatchFound() {}
    public MatchFound(String matchId, String opponentUsername, String side) {
        this.matchId = matchId;
        this.opponentUsername = opponentUsername;
        this.side = side;
    }
}
