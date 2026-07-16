package models.miniGame.beghouled;

import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;

public class BeghouledSetup implements LevelSetup {



    @Override
    public void groundSetup(GameWorld game) {

    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
