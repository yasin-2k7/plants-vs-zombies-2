package com.pvz2.models.miniGame.IZombie;

import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.card.PlantCard;
import com.pvz2.models.plant.card.PlantCardFactory;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.cellTerrains.LandTerrain;
import com.pvz2.models.world.levelSetup.LevelSetup;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.network.onlineIZombie.ZombieCard;
import com.pvz2.view.LawnGrid;

import java.util.List;

/**
 * Same board setup as IZombieSetup (grid, red-line goal brains, zombie roster), for the
 * 2-player networked match. The one deliberate difference: IZombieSetup.groundSetup()
 * auto-places random defensive plants because offline "I, Zombie" has no human plants
 * player. Here the plants player is real and plants their own — so this leaves every
 * cell empty and skips that whole block.
 */
public class OnlineIZombieSetup implements LevelSetup {
    private final int rows;
    private final int cols;
    private final List<Zombie> stageZombies;

    public OnlineIZombieSetup(int rows, int cols, List<Zombie> stageZombies) {
        this.rows = rows;
        this.cols = cols;
        this.stageZombies = stageZombies;
    }

    @Override
    public void groundSetup(GameWorld gameWorld) {
        OnlineIZombieLevel world = (OnlineIZombieLevel) gameWorld;
        world.setConveyorMode(false);
        if (world.getLawnMowerManager() != null) {
            world.getLawnMowerManager().setEnabled(false);
        }
        world.setRows(rows);
        world.setCols(cols);
        Cell[][] grid = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c, new LandTerrain());
            }
        }
        world.setGrid(grid);
        for (Zombie zombie : stageZombies){
            world.getZombieCards().add(new ZombieCard(zombie.getSpecificName(),
                getBrainCost(zombie.getSpecificName())));
        }
        world.getPlantLists().addAll(List.of(PlantCardFactory.createCard(PlantType.SUNFLOWER, 1),
            PlantCardFactory.createCard(PlantType.CABBAGE_PULT, 1),
            PlantCardFactory.createCard(PlantType.POTATO_MINE, 1),
            PlantCardFactory.createCard(PlantType.REPEATER, 1),
            PlantCardFactory.createCard(PlantType.WALL_NUT, 1),
            PlantCardFactory.createCard(PlantType.CHERRY_BOMB, 1),
            PlantCardFactory.createCard(PlantType.BONK_CHOY, 1),
            PlantCardFactory.createCard(PlantType.CITRON, 1)));

        if (world instanceof IZombieLevel level) {
            level.setAvailableZombies(stageZombies);
            for (int r = 0; r < rows; r++) {
                level.getBrains().add(new Brain(r, LawnGrid.getCellX(0) - LawnGrid.CELL_WIDTH,
                    LawnGrid.getCellY(r)));
            }
            // No auto-planted defenses, and no SunProducer seeding here — see
            // ServerGameController's class comment for why zombie income needs its
            // own decision in network mode before this gets ported over too.
        }
    }

    private int getBrainCost(String type){
        return switch (type){
            case "ZombieArmor1" -> 100;
            case "ZombieArmor2" -> 150;
            case "ZombieArmor4", "ZombieModernAllStar" -> 300;
            case "ZombieWizard" -> 200;
            case "ZombieLostCityJane" -> 75;
            case "ZombieGargantuar" -> 400;
            default -> 50;
        };
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
