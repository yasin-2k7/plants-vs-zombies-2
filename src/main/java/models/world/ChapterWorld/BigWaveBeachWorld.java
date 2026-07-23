package models.world.ChapterWorld;

import controller.GameMenuController;
import models.core.App;
import models.enums.PlantLayer;
import models.world.Cell;
import models.world.GameWorld;
import models.world.cellTerrains.LandTerrain;
import models.world.cellTerrains.WaterTerrain;
import models.world.levelSetup.BigWaveBeachLevelSetup;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.winCondition.WinCondition;
import models.zombie.Zombie;
import models.zombie.ZombieFactory;

import java.util.ArrayList;
import java.util.Random;

public class BigWaveBeachWorld extends GameWorld {
    private int tideLineCol;
    private int currentTideCol;
    private int lastTideChangeTick = 0;
    private final int tideCycleTicks = 300;
    private int lastLowLyingCoastSpawnTick = 0;
    private final int lowLyingCoastSpawnTicks = 250;
    private Random random = new Random();

    public BigWaveBeachWorld(LevelSetup levelSetup, ArrayList<LoseCondition> loseConditions, WinCondition winCondition, ArrayList<Mechanic> mechanics) {
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
        for (int i = 0; i< lowLyingCoastsCount; i++){
            makeCellLowLyingCoast();
        }
    }

    private void makeCellLowLyingCoast(){
        Random random = new Random();
        int cellRow = random.nextInt(getRows());
        int cellCol = random.nextInt(3) + getCols()-3;
        if (grid[cellRow][cellCol].isLowLyingCoast()){
            makeCellLowLyingCoast();
        }
        else{
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

            for (Cell[] cells : grid){
                for (Cell cell : cells){
                    if (cell.isLowLyingCoast()){
                        if (random.nextBoolean()){
                            Zombie zombie = random.nextBoolean() ? new ZombieFactory().createZombie("ZombieDefault") : new ZombieFactory().createZombie(App.getZombieId("ZombieConehead"));
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
            }
            else if (newTideCol > currentTideCol) {
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
                if (cell.getPlant(PlantLayer.SHIELD) != null) cell.getPlant(PlantLayer.SHIELD).die();
            } else {
                cell.setTerrain(new LandTerrain());
                if (cell.getPlant(PlantLayer.BASE) != null) cell.getPlant(PlantLayer.BASE).die();

            }
        }
    }
}
