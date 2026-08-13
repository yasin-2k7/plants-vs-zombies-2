package com.pvz2.models.world.ChapterWorld;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.levelSetup.LevelSetup;
import com.pvz2.models.world.loseCondition.LoseCondition;
import com.pvz2.models.world.mechanics.Mechanic;
import com.pvz2.models.world.winCondition.WinCondition;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.ZombieFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FrostbiteCavesWorld extends GameWorld {
    private final float icyWindTime = 5.0f;
    private float lastIcyWindTime = 0f;
    private Random random = new Random();

    private List<PlantType> lockedPlants;


    public FrostbiteCavesWorld(LevelSetup levelSetup, ArrayList<LoseCondition> loseConditions,
                               WinCondition winCondition, ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
    }


    @Override
    public void tick(float delta) {
        super.tick(delta);
        updateIcyWinds();
    }

    private void updateIcyWinds() {
        float currentTime = getElapsedTime();

        if (currentTime - lastIcyWindTime >= icyWindTime) {
            lastIcyWindTime = currentTime;
            int winds = random.nextInt(3) + 1;

            List<Integer> pool = new ArrayList<>(List.of(0, 1, 2, 3, 4));

            Collections.shuffle(pool);

            for (int i = 0; i < winds; i++) {
                int selectedRow = pool.get(i);
                GameMenuController.updateState("Ice wind in row " + (selectedRow + 1));
                for (Cell cell : grid[selectedRow]) {
                    if (cell.getPlant() != null) {
                        cell.getPlant().increaseFrozenAmount();
                    }
                }
            }
        }
    }

    private void makeCellSlippy() {
        Random random = new Random();
        int cellRow = random.nextInt(getRows());
        int cellCol = random.nextInt(3) + getCols() - 3;
        if (grid[cellRow][cellCol].getSlippingDir() != 0) {
            makeCellSlippy();
        } else {
            int dir;
            if (cellRow == 0) dir = 1;
            else if (cellRow == getRows() - 1) dir = -1;
            else dir = random.nextBoolean() ? 1 : -1;
            grid[cellRow][cellCol].setSlippingDir(dir);
        }
    }

    private void createIcyZombie() {
        Random random = new Random();
        int cellRow = random.nextInt(getRows());
        int cellCol = random.nextInt(3) + getCols() - 3;
        if (!Cell.getZombiesInCell(grid[cellRow][cellCol]).isEmpty()) {
            createIcyZombie();
        } else {
            Cell cell = grid[cellRow][cellCol];
            Zombie zombie = random.nextBoolean() ? new ZombieFactory().createZombie("ZombieDefault") :
                    new ZombieFactory().createZombie(App.getZombieId("ZombieConehead"));
            if (zombie != null) {
                zombie.setX(cell.getX());
                zombie.setY(cell.getY());
                this.addZombie(zombie);
                zombie.setIceHealth(600);
                GameMenuController.updateState("An icy zombie appeared in (" + cell.getX() + ", " + cell.getY() + ").");
            }
        }
    }

    @Override
    protected void applyChapterRules() {
        int slippingCellsCount = random.nextInt(3) + 1;
        int icyZombiesCount = random.nextInt(3);
        for (int i = 0; i < slippingCellsCount; i++) {
            makeCellSlippy();
        }
        for (int i = 0; i < icyZombiesCount; i++) {
            createIcyZombie();
        }

    }
    public void setLockedPlants(List<PlantType> lockedPlants) {
        this.lockedPlants = lockedPlants;
    }


}
