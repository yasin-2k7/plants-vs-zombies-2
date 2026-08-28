package com.pvz2.models.world.mechanics;

import com.pvz2.models.miniGame.IZombie.OnlineIZombieLevel;
import com.pvz2.models.world.GameWorld;
import com.pvz2.network.onlineIZombie.BrainCurrency;

import java.util.Random;

/**
 * The zombies' counterpart to SunSpawnMechanic: periodically drops a clickable BrainCurrency
 * pickup, collected via the same request/response shape as suns (CollectBrainRequest ->
 * ServerGameController.handleCollectBrain). Only applies to OnlineIZombieLevel — a no-op
 * on any other GameWorld, same pattern IZombieWin/IZombieLose already use for their checks.
 * Spawns only on the zombies' side of the board (columns at/past the red line), same idea
 * as suns falling across the plants' side.
 */
public class BrainSpawnMechanic implements Mechanic {
    private float lastSpawnTime = -8f;
    private final float spawnInterval;

    public BrainSpawnMechanic() {
        this(12f);
    }

    public BrainSpawnMechanic(float spawnInterval) {
        this.spawnInterval = spawnInterval;
    }

    @Override
    public void applyMechanic(GameWorld world) {
        if (!(world instanceof OnlineIZombieLevel level)) return;

        float now = level.getElapsedTime();
        if (now - lastSpawnTime >= spawnInterval) {
            spawnRandomBrain(level);
            lastSpawnTime = now;
        }
    }

    private void spawnRandomBrain(OnlineIZombieLevel level) {
        int rows = level.getRows() > 0 ? level.getRows() : 5;
        int cols = level.getCols() > 0 ? level.getCols() : 9;
        int redLineCol = level.getRedLineCol();

        if (redLineCol >= cols) return; // no room on the zombie side to spawn into

        Random random = new Random();
        int row = random.nextInt(rows);
        int col = redLineCol + random.nextInt(cols - redLineCol);

        float x = col * 100 + 50;
        float y = row * 100 + 50;
        level.getActiveBrains().add(new BrainCurrency(x, y));
    }
}
