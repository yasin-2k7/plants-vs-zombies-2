package models.world.mechanics;

import models.world.GameWorld;
import models.world.Sun;
import models.world.SunType;

import java.util.Random;

public class SunSpawnMechanic implements Mechanic{
    private long lastSpawnTime;
    private int spawnInterval = 10000; //ms

    @Override
    public void applyMechanic(GameWorld world) {
        long now = System.currentTimeMillis();

        if (now - lastSpawnTime >= spawnInterval) {
            spawnRandomSun(world);
            lastSpawnTime = now;
        }

        world.getActiveSuns().removeIf(sun -> {
            if(sun.isExpired()){
                world.getSunsPool().release(sun);
                return true;
            }
            return false;
        });
    }

    private void spawnRandomSun(GameWorld world){
        int amount = 0;
        SunType type = SunType.NORMAL;
        Random random = new Random();
        int row = random.nextInt(world.getRows());
        int col = random.nextInt(world.getCols());

        Sun sun = world.getSunsPool().acquire();
        System.out.println("DEBUG: Sun Object ID: " + System.identityHashCode(sun));
        sun.setup(row, col, amount, type);
        world.getActiveSuns().add(sun);
    }
}


