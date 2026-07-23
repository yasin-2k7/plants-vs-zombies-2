package models.world.ChapterWorld;

import controller.GameMenuController;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.winCondition.WinCondition;
import models.zombie.Zombie;
import models.zombie.ZombieFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FrostbiteCavesWorld extends GameWorld {
    private int lastIcyWindTick = 0;
    private final int icyWindTicks = 250;
    private Random random = new Random();

    public FrostbiteCavesWorld(LevelSetup levelSetup, ArrayList<LoseCondition> loseConditions, WinCondition winCondition, ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
    }


    @Override
    public void tick() {
        super.tick();
        updateIcyWinds();
    }

    private void updateIcyWinds() {
        int currentTick = getCurrentTick();

        if (currentTick - lastIcyWindTick >= icyWindTicks) {
            lastIcyWindTick = currentTick;
            int winds =  ThreadLocalRandom.current().nextInt(3) + 1;

            List<Integer> pool = new ArrayList<>(List.of(0, 1, 2, 3, 4));

            Collections.shuffle(pool);

            for (int i = 0; i < winds; i++) {
                int selectedRow = pool.get(i);
                GameMenuController.updateState("Ice wind in row " + (selectedRow+1));
                for (Cell cell : grid[selectedRow]){
                    if (cell.getPlant() != null){
                        cell.getPlant().increaseFrozenAmount();
                    }
                }
            }

        }
    }

            private void makeCellSlippy(){
        int cellRow = ThreadLocalRandom.current().nextInt(getRows());
        int cellCol =  ThreadLocalRandom.current().nextInt(3) + getCols()-2;
        if (grid[cellRow][cellCol].isLowLyingCoast()){
            makeCellSlippy();
        }
        else{
            int dir;
            if (cellRow == 0) dir = 1;
            else if (cellRow == getRows()) dir = -1;
            else dir =  ThreadLocalRandom.current().nextBoolean() ? 1 : -1;
            grid[cellRow][cellCol].setSlippingDir(dir);
        }
    }

    private void createIcyZombie(){
        int cellRow =  ThreadLocalRandom.current().nextInt(getRows());
        int cellCol =  ThreadLocalRandom.current().nextInt(3) + getCols()-2;
        if (!Cell.getZombiesInCell(grid[cellRow][cellCol]).isEmpty()){
            createIcyZombie();
        }
        else{
            Cell cell = grid[cellRow][cellCol];
            Zombie zombie =  ThreadLocalRandom.current().nextBoolean() ? new ZombieFactory().createZombie("ZombieDefault") : new ZombieFactory().createZombie("ZombieConehead");
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
        int slippingCellsCount =  ThreadLocalRandom.current().nextInt(3) + 1;
        int icyZombiesCount =  ThreadLocalRandom.current().nextInt(3);
        for (int i = 0; i< slippingCellsCount; i++){
            makeCellSlippy();
        }
        for (int i = 0; i< icyZombiesCount; i++){
            createIcyZombie();
        }

    }
}
