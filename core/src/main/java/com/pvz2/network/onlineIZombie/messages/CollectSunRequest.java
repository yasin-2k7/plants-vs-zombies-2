package com.pvz2.network.onlineIZombie.messages;

public class CollectSunRequest {
    public String matchId;
    public float x, y; // touch/click point, same convention as the single-player collectSun

    public CollectSunRequest() {}
    public CollectSunRequest(String matchId, float x, float y) {
        this.matchId = matchId;
        this.x = x;
        this.y = y;
    }
}
