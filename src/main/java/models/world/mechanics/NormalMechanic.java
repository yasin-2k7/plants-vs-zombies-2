package models.world.mechanics;

import models.core.App;
import models.core.DifficultyCalculator;
import models.world.GameWorld;
import models.zombie.Zombie;
import models.zombie.wave.WaveManager;

import java.util.Random;

public class NormalMechanic implements Mechanic{
    private WaveManager waveManager;
    private long lastZombieSpawnTick = 0;
    private int zombieSpawnInterval = 10;
    private Random random = new Random();

    public NormalMechanic(WaveManager waveManager){
        this.waveManager = waveManager;
    }

    @Override
    public void applyMechanic(GameWorld world) {
        long now = world.getCurrentTick();

        int difficulty = App.getCurrentUser().getGameDifficulty();
        double decreaseFactor = DifficultyCalculator.decreaseFactor(difficulty);
        int adjustedInterval = (int) Math.round(zombieSpawnInterval * decreaseFactor); // فاصله کمتر = سرعت بیشتر


        if(now - lastZombieSpawnTick >= adjustedInterval){
            if(!waveManager.isLevelCompleted()){
                int lane = random.nextInt(world.getRows());
                waveManager.spawnNextZombie(lane, world);
                lastZombieSpawnTick = now;
            }
        }

        waveManager.update();

        world.getActiveZombies().stream()
                .filter(Zombie::isDead)
                .forEach(zombie -> {
                    waveManager.onZombieKilled(zombie);
                    world.notifyZombieKilled();
                });

    }

    public WaveManager getWaveManager(){
        return waveManager;
    }
}
