package models.world.levelsSpecial;

import models.enums.PlantType;
import models.world.GameWorld;

import java.util.List;

public class LockedPlantLevel extends GameWorld {
    private List<PlantType> lockedPlants;
    private List<PlantType> forcedPlants;

    @Override
    protected void applyChapterRules() {

    }

    public void setLockedPlants(List<PlantType> lockedPlants) {
        this.lockedPlants = lockedPlants;
    }

    public void setForcedPlants(List<PlantType> forcedPlants) {
        this.forcedPlants = forcedPlants;
    }
}
