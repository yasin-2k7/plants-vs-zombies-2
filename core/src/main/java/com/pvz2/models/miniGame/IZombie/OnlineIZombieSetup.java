package com.pvz2.models.miniGame.IZombie;

import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.cellTerrains.LandTerrain;
import com.pvz2.models.world.levelSetup.LevelSetup;
import com.pvz2.models.zombie.Zombie;

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
    public void groundSetup(GameWorld world) {
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

        if (world instanceof IZombieLevel level) {
            level.setAvailableZombies(stageZombies);
            for (int r = 0; r < rows; r++) {
                level.getBrains().add(new Brain(r, 10, r * 100 + 50));
            }
            // No auto-planted defenses, and no SunProducer seeding here — see
            // ServerGameController's class comment for why zombie income needs its
            // own decision in network mode before this gets ported over too.
        }
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
