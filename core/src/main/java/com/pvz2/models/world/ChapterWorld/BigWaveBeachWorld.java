package com.pvz2.models.world.ChapterWorld;

import controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantLayer;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.cellTerrains.LandTerrain;
import com.pvz2.models.world.cellTerrains.WaterTerrain;
import com.pvz2.models.world.levelSetup.BigWaveBeachLevelSetup;
import com.pvz2.models.world.levelSetup.LevelSetup;
import com.pvz2.models.world.loseCondition.LoseCondition;
import com.pvz2.models.world.mechanics.Mechanic;
import com.pvz2.models.world.winCondition.WinCondition;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.ZombieFactory;

import java.util.ArrayList;
import java.util.Random;

public class BigWaveBeachWorld extends GameWorld {
    private final int tideCycleTicks = 300;
    private final int lowLyingCoastSpawnTicks = 250;
    private int tideLineCol;
    private int currentTideCol;
    private int lastTideChangeTick = 0;
    private int lastLowLyingCoastSpawnTick = 0;
    private Random random = new Random();

    public BigWaveBeachWorld(LevelSetup levelSetup, ArrayList<LoseCondition> loseConditions,
                             WinCondition winCondition, ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
    }

    @Override
    protected void applyChapterRules() {
        Random random = new Random();
        if (getLevelSetup() instanceof BigWaveBeachLevelSetup beachSetup) {
            tideLineCol = beachSetup.getTideLineCol();
            this.currentTideCol = 9;
        }
        int lowLyingCoastsCount = random.nextInt(4) + 1;
        for (int i = 0; i < lowLyingCoastsCount; i++) {
            makeCellLowLyingCoast();
        }
    }

    private void makeCellLowLyingCoast() {
        Random random = new Random();
        int cellRow = random.nextInt(getRows());
        int cellCol = random.nextInt(3) + getCols() - 3;
        if (grid[cellRow][cellCol].isLowLyingCoast()) {
            makeCellLowLyingCoast();
        } else {
            grid[cellRow][cellCol].setLowLyingCoast(true);
        }
    }

    @Override
    public void tick() {
        super.tick();
        updateLowLyingCoasts();
        updateTide();
    }

    private void updateLowLyingCoasts() {
        int currentTick = getCurrentTick();

        if (currentTick - lastLowLyingCoastSpawnTick >= lowLyingCoastSpawnTicks) {
            lastLowLyingCoastSpawnTick = currentTick;

            for (Cell[] cells : grid) {
                for (Cell cell : cells) {
                    if (cell.isLowLyingCoast()) {
                        if (random.nextBoolean()) {
                            Zombie zombie = random.nextBoolean() ?
                                    new ZombieFactory().createZombie("ZombieDefault") :
                                    new ZombieFactory().createZombie(App.getZombieId("ZombieConehead"));
                            if (zombie != null) {
                                zombie.setX(cell.getX());
                                zombie.setY(cell.getY());
                                this.addZombie(zombie);
                                GameMenuController.updateState("A zombie emerged from a low lying coast.");
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateTide() {
        int currentTick = getCurrentTick();

        if (currentTick - lastTideChangeTick >= tideCycleTicks) {
            lastTideChangeTick = currentTick;

            int minCol = tideLineCol;
            int maxCol = getCols();
            int newTideCol = random.nextInt(maxCol - minCol + 1) + minCol;

            if (newTideCol < currentTideCol) {
                for (int c = newTideCol; c < currentTideCol; c++) {
                    changeColumnTerrain(c, true);
                }
                GameMenuController.updateState("tide rising...");
            } else if (newTideCol > currentTideCol) {
                for (int c = currentTideCol; c < newTideCol; c++) {
                    changeColumnTerrain(c, false);
                }
                GameMenuController.updateState("tide receding...");

            }

            currentTideCol = newTideCol;
        }
    }


    private void changeColumnTerrain(int col, boolean makeWater) {
        Cell[][] grid = getGrid();
        for (int r = 0; r < getRows(); r++) {
            Cell cell = grid[r][col];
            if (makeWater) {
                cell.setTerrain(new WaterTerrain());
                if (cell.getPlant(PlantLayer.MAIN) != null) cell.getPlant(PlantLayer.MAIN).die();
                if (cell.getPlant(PlantLayer.SHIELD) != null)
                    cell.getPlant(PlantLayer.SHIELD).die();
            } else {
                cell.setTerrain(new LandTerrain());
                if (cell.getPlant(PlantLayer.BASE) != null) cell.getPlant(PlantLayer.BASE).die();

            }
        }
    }
}
