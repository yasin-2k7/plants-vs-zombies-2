package models.world.mechanics;

import models.world.GameWorld;
import models.zombie.Zombie;
import models.zombie.wave.WaveManager;

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

        for (Zombie _ : deadZombies) {
            waveManager.onZombieKilled();
            world.notifyZombieKilled();
        }

        world.getActiveZombies().removeAll(deadZombies);

    }

    public WaveManager getWaveManager() {
        return waveManager;
    }
}
