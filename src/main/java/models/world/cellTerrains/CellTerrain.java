package models.world.cellTerrains;

import models.plant.Plant;
import models.world.Cell;

public interface CellTerrain {
    boolean canPlant(Plant plant, Cell cell);
    boolean isWater();
    String getTerminalSymbol();
}
