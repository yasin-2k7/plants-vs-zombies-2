package models.world.mechanics;

import models.core.App;
import models.core.DifficultyCalculator;
import models.world.GameWorld;
import models.world.Sun;
import models.world.SunType;

import java.util.Random;

public class SunSpawnMechanic implements Mechanic{
    private long lastSpawnTick = 0;
    private int spawnInterval = 10; //ms

    @Override
    public void applyMechanic(GameWorld world) {
        long now = world.getCurrentTick();

        int difficulty = App.getCurrentUser().getGameDifficulty();
        double increaseFactor = DifficultyCalculator.increaseFactor(difficulty);
        int adjustedInterval = (int) Math.round(spawnInterval * increaseFactor);


        if (now - lastSpawnTick >= adjustedInterval) {
            spawnRandomSun(world);
            lastSpawnTick = now;
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


