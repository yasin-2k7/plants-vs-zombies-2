package com.pvz2.network.onlineIZombie.messages;

public class ChallengeResponse {
    public boolean success;      // true means "invite delivered, now wait for MatchFound or ChallengeDeclined"
    public String errorMessage;  // set when success is false: invalid username / user offline / already in a match / self-challenge
    public String inviteId;      // set when success is true, lets the client correlate a later cancel if you add one

    public ChallengeResponse() {}
}
