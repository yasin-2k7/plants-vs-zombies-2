package models.world.levelSetup;

import models.world.GameWorld;

public interface LevelSetup {
    void groundSetup(GameWorld game);
    boolean requirePlantSelection();
}
