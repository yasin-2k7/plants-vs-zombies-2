package models.plant.components.explosionRanges;

import models.plant.Plant;
import models.world.Cell;
import models.zombie.Zombie;

import java.util.List;

public interface ExplosionRange {
    List<Cell> getCells(Plant owner);
}
