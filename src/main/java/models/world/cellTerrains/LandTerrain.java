package models.world.cellTerrains;

import models.enums.PlantType;
import models.plant.Plant;
import models.plant.components.PlacementBehaviorComponent;
import models.world.Cell;

public class LandTerrain implements CellTerrain {
    @Override
    public boolean canPlant(Plant plant, Cell cell) {
        if (plant.getType() == PlantType.HOT_POTATO) {
            return  !cell.isEmpty() && cell.getPlant().isFreeze();
        }
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
