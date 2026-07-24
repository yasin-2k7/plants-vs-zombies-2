package models.world.levelSetup;

import models.world.GameWorld;
import models.world.mechanics.NormalMechanic;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;

import java.util.List;

public class NightOpsLevelSetup implements LevelSetup {
    private int rows;
    private int cols;
    private List<Wave> waves;

    public NightOpsLevelSetup(int rows, int cols, List<Wave> waves) {
        this.rows = rows;
        this.cols = cols;
        this.waves = waves;
    }

    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(false);
        buildGrid(world, rows, cols);

        WaveManager waveManager = new WaveManager(waves);
        world.addMechanic(new NormalMechanic(waveManager));

    }

    @Override
    public boolean requirePlantSelection() {
        return true;
    }
}
