package models.plant.components.explosionRanges;

import controller.LevelMenuController;
import models.plant.Plant;
import models.world.Cell;

import java.util.List;

public class LineRange implements ExplosionRange{
    public static final LineRange INSTANCE = new LineRange();
    private LineRange() {}


    @Override
    public List<Cell> getCells(Plant owner) {
        return Cell.getCellsInRow(owner.getCell(), LevelMenuController.getGameCells());
    }
}
