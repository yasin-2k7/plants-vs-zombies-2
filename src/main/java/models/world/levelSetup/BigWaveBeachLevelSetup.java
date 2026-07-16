package models.world.levelSetup;

import models.world.Cell;
import models.world.GameWorld;
import models.world.cellTerrains.WaterTerrain;

public class BigWaveBeachLevelSetup implements LevelSetup{
    private final int tideLineCol;

    public BigWaveBeachLevelSetup(int tideLineCol) {
        this.tideLineCol = tideLineCol;
    }

    @Override
    public void groundSetup(GameWorld game) {
        buildGrid(game, 5, 9);
        Cell[][] grid = game.getGrid();
        for (int r = 0; r < game.getRows(); r++) {
            grid[r][game.getCols()-1].setTerrain(new WaterTerrain());
        }
    }

    @Override
    public boolean requirePlantSelection() {
        return true;
    }

    public int getTideLineCol() {
        return tideLineCol;
    }
}
