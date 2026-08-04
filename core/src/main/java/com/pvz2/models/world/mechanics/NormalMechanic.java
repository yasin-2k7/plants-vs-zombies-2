package com.pvz2.models.world.mechanics;

import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.wave.WaveManager;

import java.util.List;
import java.util.Random;

public class NormalMechanic implements Mechanic {
    private WaveManager waveManager;
    private Random random = new Random();

    public NormalMechanic(WaveManager waveManager) {
        this.waveManager = waveManager;
    }

    @Override
    public void applyMechanic(GameWorld world) {


        if (!waveManager.update()) {
            waveManager.spawnNextZombie(random.nextInt(world.getRows()), world);
        }


        List<Zombie> deadZombies = world.getActiveZombies().stream()
                .filter(Zombie::isDead)
                .toList();

        for (Zombie zombie : deadZombies) {
            waveManager.onZombieKilled();
            world.notifyZombieKilled();
        }

        world.getActiveZombies().removeAll(deadZombies);

    }

    public WaveManager getWaveManager() {
        return waveManager;
    }
}
