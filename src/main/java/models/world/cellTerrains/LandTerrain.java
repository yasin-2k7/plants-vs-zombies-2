package models.world.cellTerrains;

import models.plant.Plant;
import models.plant.components.PlacementBehaviorComponent;
import models.world.Cell;

public class LandTerrain implements CellTerrain {
    @Override
    public boolean canPlant(Plant plant, Cell cell) {
        PlacementBehaviorComponent behavior = plant.getComponent(PlacementBehaviorComponent.class);

        if (behavior != null && behavior.isWaterOnly()) {
            return false;
        }

        return true;
    }


    @Override
    public boolean isWater() {
        return false;
    }

    @Override
    public String getTerminalSymbol() {
        return ".";
    }
}
