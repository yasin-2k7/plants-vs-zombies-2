package com.pvz2.network.onlineIZombie.messages;

public class PlaceZombieRequest {
    public String matchId;
    public String zombieType; // TODO: swap for your real ZombieType enum once that class is available
    public float x, y;

    public PlaceZombieRequest() {}
    public PlaceZombieRequest(String matchId, String zombieType, float x, float y) {
        this.matchId = matchId;
        this.zombieType = zombieType;
        this.x = x;
        this.y = y;
    }
}
