package com.pvz2.models.network.onlineIZombie.messages;

public class ChallengeRequest {
    public String opponentUsername;

    public ChallengeRequest() {}
    public ChallengeRequest(String opponentUsername) { this.opponentUsername = opponentUsername; }
}
