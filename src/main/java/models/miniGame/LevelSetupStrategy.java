package models.miniGame;

import models.world.GameWorld;

public interface LevelSetupStrategy {
    void setupBoard(GameWorld world);
}
