package models.world.mechanics;

import models.world.GameWorld;
import models.world.Sun;
import models.world.SunType;

import java.util.Random;

public class SunSpawnMechanic implements Mechanic{
    private long lastSpawnTick = 0;
    private int spawnInterval = 120; //ms

    @Override
    public void applyMechanic(GameWorld world) {
        long now = world.getCurrentTick();

        if (now - lastSpawnTick >= spawnInterval) {
            spawnRandomSun(world);
            spawnInterval = Math.max((int) (6+0.05* world.getCurrentTick()), 120);
            lastSpawnTick = now;
        }

    }

    private void spawnRandomSun(GameWorld world){
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


