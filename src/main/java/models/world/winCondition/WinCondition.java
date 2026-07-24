package models.world.winCondition;

import models.miniGame.MiniGameLevels;
import models.world.GameWorld;

public interface WinCondition {
    boolean checkWin(GameWorld game);

    void setCurrentLevel(MiniGameLevels currentLevel);
}
