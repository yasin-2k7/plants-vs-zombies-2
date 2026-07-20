package models.miniGame.bowling;

import models.world.Cell;
import models.world.GameWorld;
import models.world.cellTerrains.LandTerrain;
import models.world.levelSetup.LevelSetup;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;
import models.zombie.wave.WaveSpawnEntry;
import models.world.mechanics.NormalMechanic;

import java.util.List;

public class BowlingSetup implements LevelSetup {
    private final int rows;
    private final int cols;
    private final int redLineCol;
    private final List<WaveSpawnEntry> availableZombies;
    private final int waveCount;
    private final int baseDifficulty;

    public BowlingSetup(int rows, int cols, int redLineCol,
                        List<WaveSpawnEntry> availableZombies,
                        int waveCount, int baseDifficulty) {
        this.rows = rows;
        this.cols = cols;
        this.redLineCol = redLineCol;
        this.availableZombies = availableZombies;
        this.waveCount = waveCount;
        this.baseDifficulty = baseDifficulty;
    }

    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(true);
        world.setRows(rows);
        world.setCols(cols);

        Cell[][] grid = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c, new LandTerrain());
                if (c >= redLineCol) {
                    grid[r][c].setPlantable(false);
                }
            }
        }
        world.setGrid(grid);

        world.addMechanic(new BowlingMechanics());

        List<Wave> waves = Wave.generateWaves(waveCount, baseDifficulty, availableZombies, 40);
        WaveManager waveManager = new WaveManager(waves);
        world.addMechanic(new NormalMechanic(waveManager));
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}