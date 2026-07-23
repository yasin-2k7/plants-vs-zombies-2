package models.plant.components.explosionRanges;

import models.plant.Plant;
import models.world.Cell;

import java.util.List;

public interface ExplosionRange {
    List<Cell> getCells(Plant owner);
}
