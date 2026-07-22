package models.world.levelsSpecial;

import models.enums.PlantType;
import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.winCondition.WinCondition;

import java.util.ArrayList;
import java.util.List;

public class LockedPlantLevel extends GameWorld {
    private List<PlantType> lockedPlants;
    private List<PlantType> forcedPlants;

    public LockedPlantLevel(LevelSetup levelSetup,
                            ArrayList<LoseCondition> loseConditions,
                            WinCondition winCondition,
                            ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
    }

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
