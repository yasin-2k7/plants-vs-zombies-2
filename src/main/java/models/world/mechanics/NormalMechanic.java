package models.world.mechanics;

import models.world.GameWorld;
import models.zombie.Zombie;
import models.zombie.wave.WaveManager;

import java.util.Random;

public class NormalMechanic implements Mechanic{
    private WaveManager waveManager;
    private long lastZombieSpawnTime = 0;
    private int zombieSpawnInterval;
    private Random random = new Random();

    public NormalMechanic(WaveManager waveManager){
        this.waveManager = waveManager;
    }

    @Override
    public void applyMechanic(GameWorld world) {
//        long now = System.currentTimeMillis();
//
//        if(now - lastZombieSpawnTime >= zombieSpawnInterval){
//            if(!waveManager.isLevelCompleted()){
//                int lane = random.nextInt(world.getRows());
//                waveManager.spawnNextZombie(lane, world);
//                lastZombieSpawnTime = now;
//            }
//        }

        if (!waveManager.update()){
            waveManager.spawnNextZombie(random.nextInt(world.getRows()), world);
        }


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
