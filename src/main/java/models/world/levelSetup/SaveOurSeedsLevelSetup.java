package models.world.levelSetup;

import models.world.GameWorld;

public class SaveOurSeedsLevelSetup implements LevelSetup{
    @Override
    public void groundSetup(GameWorld game) {

    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
