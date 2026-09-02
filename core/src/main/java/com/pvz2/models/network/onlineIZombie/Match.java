package com.pvz2.models.network.onlineIZombie;

import com.pvz2.models.network.ClientHandler;

import java.util.UUID;

/**
 * A live "I, Zombie" 1v1 session between two connected players.
 * Created the moment a challenge is accepted or a random pairing is made;
 * side assignment (who plants, who sends zombies) happens here, once, at creation.
 */
public class Match {
    public enum Side { PLANTS, ZOMBIES }

    private final String matchId;
    private final ClientHandler plantsPlayer;
    private final ClientHandler zombiesPlayer;

    public Match(ClientHandler playerA, ClientHandler playerB) {
        this.matchId = UUID.randomUUID().toString();
        // coin flip for who defends vs. who attacks — spec doesn't require anything specific here
        if (Math.random() < 0.5) {
            this.plantsPlayer = playerA;
            this.zombiesPlayer = playerB;
        } else {
            this.plantsPlayer = playerB;
            this.zombiesPlayer = playerA;
        }
    }

    public String getMatchId() { return matchId; }
    public ClientHandler getPlantsPlayer() { return plantsPlayer; }
    public ClientHandler getZombiesPlayer() { return zombiesPlayer; }

    public ClientHandler getOpponentOf(ClientHandler player) {
        if (player == plantsPlayer) return zombiesPlayer;
        if (player == zombiesPlayer) return plantsPlayer;
        return null;
    }

    public Side getSideOf(ClientHandler player) {
        if (player == plantsPlayer) return Side.PLANTS;
        if (player == zombiesPlayer) return Side.ZOMBIES;
        return null;
    }

    public boolean hasPlayer(ClientHandler player) {
        return player == plantsPlayer || player == zombiesPlayer;
    }
}
