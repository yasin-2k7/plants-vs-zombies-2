package models.world.levelSetup;

import models.world.GameWorld;
import models.world.levelsSpecial.DeadLineLevel;
import models.world.mechanics.NormalMechanic;
import models.world.mechanics.SunSpawnMechanic;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;

import java.util.List;

public class DeadLineLevelSetup implements LevelSetup{
    private int rows;
    private int cols;
    private int deadLineCol;
    private List<Wave> waves;

    public DeadLineLevelSetup(int rows, int cols, int deadLineCol, List<Wave> waves){
        this.rows = rows;
        this.cols = cols;
        this.deadLineCol = deadLineCol;
        this.waves = waves;
    }

    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(false);
        buildGrid(world, rows, cols);

        ((DeadLineLevel) world).setDeadLineCol(deadLineCol);

        WaveManager waveManager = new WaveManager(waves);
        world.addMechanic(new NormalMechanic(waveManager));
        world.addMechanic(new SunSpawnMechanic());

    }

    @Override
    public boolean requirePlantSelection() {
        return true;
    }
}
