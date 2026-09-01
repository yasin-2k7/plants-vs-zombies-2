package com.pvz2.models.network.onlineIZombie.messages;

public class ChallengeAnswerResponse {
    public boolean success;
    public String errorMessage; // e.g. "This invite is no longer valid" if the challenger disconnected meanwhile

    public ChallengeAnswerResponse() {}
    public ChallengeAnswerResponse(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }
}
