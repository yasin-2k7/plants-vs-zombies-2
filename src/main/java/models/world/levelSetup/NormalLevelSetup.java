package models.world.levelSetup;

import models.world.Cell;
import models.world.GameWorld;

public class NormalLevelSetup implements LevelSetup{
    private int rows;
    private int cols;

    public NormalLevelSetup(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(false);
        world.setRows(rows);
        world.setCols(cols);

        Cell[][] grid = new Cell[rows][cols];
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                grid[r][c] = new Cell(r, c);
            }
        }
        world.setGrid(grid);
    }

    @Override
    public boolean requirePlantSelection() {
        return true;
    }
}
