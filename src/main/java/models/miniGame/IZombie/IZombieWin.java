package models.miniGame.IZombie;

import models.core.App;
import models.core.UserDataManager;
import models.miniGame.MiniGameLevels;
import models.world.GameWorld;
import models.world.winCondition.WinCondition;

public class IZombieWin implements WinCondition {
    MiniGameLevels currentLevel;

    @Override
    public boolean checkWin(GameWorld game) {
        if (game instanceof IZombieLevel level) {
            if (level.getBrains().isEmpty()) return false;
            for (Brain brain : level.getBrains()) {
                if (!brain.isEaten()) return false;
            }
            if (currentLevel != null) App.getCurrentUser().getMiniGameLevels().add(currentLevel);
            UserDataManager.saveUser(App.getCurrentUser());

            return true;
        }
        return false;
    }

    @Override
    public void setCurrentLevel(MiniGameLevels currentLevel) {
        this.currentLevel = currentLevel;
    }
}
