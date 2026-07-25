package models.world.levelSetup;

import models.world.Cell;
import models.world.GameWorld;
import models.world.cellTerrains.WaterTerrain;
import models.world.mechanics.NormalMechanic;
import models.world.mechanics.SunSpawnMechanic;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;

import java.util.List;

public class BigWaveBeachLevelSetup implements LevelSetup {
    private final int tideLineCol;
    private int rows;
    private int cols;
    private List<Wave> waves;

    public BigWaveBeachLevelSetup(int tideLineCol, int rows, int cols, List<Wave> waves) {
        this.tideLineCol = tideLineCol;
        this.rows = rows;
        this.cols = cols;
        this.waves = waves;
    }

    @Override
    public void groundSetup(GameWorld game) {
        buildGrid(game, rows, cols);
        Cell[][] grid = game.getGrid();
        for (int r = 0; r < game.getRows(); r++) {
            grid[r][game.getCols() - 1].setTerrain(new WaterTerrain());
        }

        WaveManager waveManager = new WaveManager(waves);
        game.addMechanic(new NormalMechanic(waveManager));
        game.addMechanic(new SunSpawnMechanic());
        game.registerZombieKillListener(() -> waveManager.onZombieKilled());

    }

    @Override
    public boolean requirePlantSelection() {
        return true;
    }

    public int getTideLineCol() {
        return tideLineCol;
    }
}
