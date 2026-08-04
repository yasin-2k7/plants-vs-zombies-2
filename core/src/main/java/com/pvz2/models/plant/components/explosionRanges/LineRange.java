package com.pvz2.models.plant.components.explosionRanges;

import controller.LevelMenuController;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;

import java.util.List;

public class LineRange implements ExplosionRange {
    public static final LineRange INSTANCE = new LineRange();

    private LineRange() {
    }


    @Override
    public List<Cell> getCells(Plant owner) {
        return Cell.getCellsInRow(owner.getCell(), LevelMenuController.getGameCells());
    }
}
