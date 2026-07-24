package models.plant.components.explosionRanges;

import controller.LevelMenuController;
import models.plant.Plant;
import models.plant.components.ExplosivesComponent;
import models.world.Cell;

import java.util.List;


public class CircularRange implements ExplosionRange {
    private final int radius;

    public CircularRange(int radius) {
        this.radius = radius;
    }

    @Override
    public List<Cell> getCells(Plant owner) {
        if (owner.getComponent(ExplosivesComponent.class) == null ||
                owner.getComponent(ExplosivesComponent.class).getTarget() == null) {
            return Cell.getNeighborCells(owner.getCell(), LevelMenuController.getGameCells(), radius);
        }
        return Cell.getNeighborCells(owner.getComponent(ExplosivesComponent.class).getTarget(),
                LevelMenuController.getGameCells(), radius);
    }
}
