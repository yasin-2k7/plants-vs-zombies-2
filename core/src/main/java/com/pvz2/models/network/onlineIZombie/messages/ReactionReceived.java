package com.pvz2.models.network.onlineIZombie.messages;

/** Pushed (no requestId) to the OPPONENT only — the sender doesn't need to see their own
 *  reaction echoed back, per the spec (it's shown on the opponent's screen, e.g. a corner). */
public class ReactionReceived {
    public String matchId;
    public String fromUsername;
    public ReactionCategory category;
    public int index;

    public ReactionReceived() {}
    public ReactionReceived(String matchId, String fromUsername, ReactionCategory category, int index) {
        this.matchId = matchId;
        this.fromUsername = fromUsername;
        this.category = category;
        this.index = index;
    }
}
