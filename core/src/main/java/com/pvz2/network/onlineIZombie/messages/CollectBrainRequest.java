package com.pvz2.network.onlineIZombie.messages;

public class CollectBrainRequest {
    public String matchId;
    public float x, y; // click point, same convention as CollectSunRequest

    public CollectBrainRequest() {}
    public CollectBrainRequest(String matchId, float x, float y) {
        this.matchId = matchId;
        this.x = x;
        this.y = y;
    }
}
