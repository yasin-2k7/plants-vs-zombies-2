package com.pvz2.models.network.onlineIZombie.messages;

public class SendReactionRequest {
    public String matchId;
    public ReactionCategory category;
    public int index; // 0-2, position in that category's fixed 3-item preset list

    public SendReactionRequest() {}
    public SendReactionRequest(String matchId, ReactionCategory category, int index) {
        this.matchId = matchId;
        this.category = category;
        this.index = index;
    }
}
