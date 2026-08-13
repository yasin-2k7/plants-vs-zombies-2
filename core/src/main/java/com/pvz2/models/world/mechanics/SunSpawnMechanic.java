package com.pvz2.models.world.mechanics;

import com.pvz2.models.core.App;
import com.pvz2.models.core.DifficultyCalculator;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.Sun;
import com.pvz2.models.world.SunType;

import java.util.Random;

public class SunSpawnMechanic implements Mechanic {
    private float lastSpawnTime = 0f;
    private float spawnInterval = 12f; //ms

    @Override
    public void applyMechanic(GameWorld world) {
        float now = world.getElapsedTime();

        int difficulty = App.getCurrentUser().getGameDifficulty();
        double increaseFactor = DifficultyCalculator.increaseFactor(difficulty);
        int adjustedInterval = (int) Math.round(spawnInterval * increaseFactor);


        if (now - lastSpawnTime >= adjustedInterval) {
            spawnRandomSun(world);
            spawnInterval = Math.max((float) (6f + 0.05f * world.getElapsedTime()), 12f);
            lastSpawnTime = now;
        }

    }

    private void spawnRandomSun(GameWorld world) {
        SunType type;
        Random random = new Random();
        double r = random.nextDouble();

        if (r < 0.80) {
            type = SunType.NORMAL;
        } else if (r < 0.95) {
            type = SunType.SPECIAL;
        } else {
            type = SunType.RADIOACTIVE;
        }
        int row = random.nextInt(world.getRows());
        int col = random.nextInt(world.getCols());

        Sun sun = world.getSunsPool().acquire();
        sun.setup(row, col, type);
        world.getActiveSuns().add(sun);
    }
}


