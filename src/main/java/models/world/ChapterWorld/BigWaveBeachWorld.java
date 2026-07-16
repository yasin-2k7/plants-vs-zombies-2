package models.world.ChapterWorld;

import models.enums.PlantLayer;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.world.cellTerrains.LandTerrain;
import models.world.cellTerrains.WaterTerrain;
import models.world.levelSetup.BigWaveBeachLevelSetup;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.winCondition.WinCondition;

import java.util.ArrayList;

public class BigWaveBeachWorld extends GameWorld {
    private int tideLineCol;
    private int currentTideCol;
    private int lastTideChangeTick = 0;
    private final int tideCycleTicks = 300;
    private boolean isWaterRising = true;

    public BigWaveBeachWorld(LevelSetup levelSetup, ArrayList<LoseCondition> loseConditions, WinCondition winCondition, ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
    }

    @Override
    protected void applyChapterRules() {
        if (getLevelSetup() instanceof BigWaveBeachLevelSetup beachSetup) {
            tideLineCol = beachSetup.getTideLineCol();
            this.currentTideCol = 9;
        }
    }

    @Override
    public void tick() {
        super.tick();

        updateTide();
    }

    private void updateTide() {
        int currentTick = getCurrentTick();

        if (currentTick - lastTideChangeTick >= tideCycleTicks) {
            lastTideChangeTick = currentTick;

            if (isWaterRising) {
                riseTide();
            } else {
                recedeTide();
            }
            isWaterRising = !isWaterRising;
        }
    }

    private void riseTide() {
        for (int c = getCols() - 1; c >= tideLineCol; c--) {
            changeColumnTerrain(c, true);
        }
        currentTideCol = tideLineCol;
    }

    private void recedeTide() {
        int defaultWaterCol = getCols() - 2;
        for (int c = tideLineCol; c < defaultWaterCol; c++) {
            changeColumnTerrain(c, false);
        }
        currentTideCol = defaultWaterCol;
    }

    private void changeColumnTerrain(int col, boolean makeWater) {
        Cell[][] grid = getGrid();
        for (int r = 0; r < getRows(); r++) {
            Cell cell = grid[r][col];
            if (makeWater) {
                cell.setTerrain(new WaterTerrain());
                if (cell.getPlant(PlantLayer.MAIN) != null) cell.getPlant(PlantLayer.MAIN).die();
                if (cell.getPlant(PlantLayer.SHIELD) != null) cell.getPlant(PlantLayer.SHIELD).die();
            } else {
                cell.setTerrain(new LandTerrain());
                if (cell.getPlant(PlantLayer.BASE) != null) cell.getPlant(PlantLayer.BASE).die();

            }
        }
    }
}
