package models.miniGame.bowling;

import models.enums.PlantType;
import models.plant.card.PlantCard;
import models.world.Cell;
import models.world.GameWorld;
import models.world.cellTerrains.LandTerrain;
import models.world.levelSetup.LevelSetup;
import models.world.mechanics.ConveyorMechanic;
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

        List<PlantCard> bowlingCards = List.of(
                new PlantCard(PlantType.WALL_NUT, 0, 0),
                new PlantCard(PlantType.EXPLODE_O_NUT, 0, 0),
                new PlantCard(PlantType.GIANT_WALLNUT, 0, 0)
        );

        world.addMechanic(new ConveyorMechanic(bowlingCards));

        List<Wave> waves = Wave.generateWaves(waveCount, baseDifficulty, availableZombies, 40);
        WaveManager waveManager = new WaveManager(waves);
        world.addMechanic(new NormalMechanic(waveManager));
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}