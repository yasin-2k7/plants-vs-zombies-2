package com.pvz2.models.network.onlineIZombie.messages;

import com.pvz2.models.miniGame.IZombie.OnlineIZombieLevel;
import com.pvz2.models.world.GameWorld;

/**
 * Pushed (no requestId) to both players after any validated gameplay action.
 * Whole-world replace — simplest correct option at course-project scale/frequency.
 * If it ever gets too chatty, switch to a smaller delta payload without changing
 * anything about how the request/response side works.
 */
public class GameStateUpdate {
    public String matchId;
    public OnlineIZombieLevel world;

    public GameStateUpdate() {}
    public GameStateUpdate(String matchId, OnlineIZombieLevel world) {
        this.matchId = matchId;
        this.world = world;
    }
}
