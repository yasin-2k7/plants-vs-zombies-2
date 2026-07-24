package models.world.cellTerrains;

import models.enums.PlantLayer;
import models.plant.Plant;
import models.plant.components.PlacementBehaviorComponent;
import models.world.Cell;

public class WaterTerrain implements CellTerrain {
    @Override
    public boolean canPlant(Plant plant, Cell cell) {
        PlacementBehaviorComponent behavior = plant.getComponent(PlacementBehaviorComponent.class);

        if (behavior != null) {
            if (behavior.isWaterOnly()) {
                return true;
            }
            return !cell.isLayerEmpty(PlantLayer.BASE);
        }

        return !cell.isLayerEmpty(PlantLayer.BASE);
    }

    @Override
    public boolean isWater() {
        return true;
    }

    @Override
    public String getTerminalSymbol() {
        return "~";
    }
}
