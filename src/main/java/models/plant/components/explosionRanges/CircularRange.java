package models.plant.components.explosionRanges;

import controller.LevelMenuController;
import models.core.App;
import models.plant.Plant;
import models.world.Cell;
import models.zombie.Zombie;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CircularRange implements ExplosionRange{
    private final int radius;

    public CircularRange(int radius) {
        this.radius = radius;
    }

    @Override
    public List<Cell> getCells(Plant owner) {
        return Cell.getNeighborCells(owner.getCell(), LevelMenuController.getGameCells(), radius);
    }
}
