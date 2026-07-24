package models.world.mechanics;

import controller.GameMenuController;
import models.world.Cell;
import models.world.ChapterWorld.DarkAgesWorld;
import models.world.GameWorld;
import models.zombie.Zombie;
import models.zombie.ZombieFactory;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;

public class DarkAgesMechanic implements Mechanic {
    private int lastWaveNumber = -1;

    @Override
    public void applyMechanic(GameWorld world) {
        if (!(world instanceof DarkAgesWorld)) return;
        DarkAgesWorld darkWorld = (DarkAgesWorld) world;

        NormalMechanic normalMechanic = world.getMechanic(NormalMechanic.class);
        if (normalMechanic == null) return;
        WaveManager waveManager = normalMechanic.getWaveManager();
        if (waveManager == null) return;

        Wave currentWave = waveManager.getCurrentWave();
        if (currentWave == null) return;

        int currentWaveNumber = currentWave.getWaveNumber();

        if (currentWaveNumber != lastWaveNumber) {
            lastWaveNumber = currentWaveNumber;

            darkWorld.spawnWaveGraves();

            darkWorld.triggerNecromancy();

            spawnZombiesFromNecromancy(darkWorld);
        }
    }

    private void spawnZombiesFromNecromancy(DarkAgesWorld world) {
        Cell[][] grid = world.getGrid();
        for (int r = 0; r < world.getRows(); r++) {
            for (int c = 0; c < world.getCols(); c++) {
                Cell cell = grid[r][c];
                if (cell.isNecromancyTriggered()) {
                    Zombie zombie = new ZombieFactory().createZombie("ZombieDefault");
                    if (zombie != null) {
                        float x = c * 100f + 50f;
                        float y = r * 100f + 50f;
                        zombie.setX(x);
                        zombie.setY(y);
                        world.addZombie(zombie);
                        GameMenuController.updateState("A zombie emerged from a grave at (" + c + ", " + r + ")");
                    }
                    cell.setNecromancyTriggered(false);
                }
            }
        }
    }
}