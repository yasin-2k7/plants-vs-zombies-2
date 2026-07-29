package models.miniGame.vaseBreaker;

import models.core.App;
import models.core.UserDataManager;
import models.miniGame.MiniGameLevels;
import models.world.GameWorld;
import models.world.winCondition.WinCondition;

public class VaseBreakerWinCondition implements WinCondition {
    MiniGameLevels currentLevel;

    @Override
    public boolean checkWin(GameWorld gameWorld) {
        if (gameWorld instanceof VaseBreakerLevel level) {

            for (Vase vase : level.getVases()) {
                if (!vase.isBroken()) {
                    return false;
                }
            }
            if (level.getActiveZombies().isEmpty()) {
                if (currentLevel != null){
                    App.getCurrentUser().getMiniGameLevels().add(currentLevel);
                    if (currentLevel.level != 3) App.getCurrentUser().notifyMinigameUnlocked(
                            currentLevel.miniGame.name() + " " + (currentLevel.level+1));
                }
                UserDataManager.saveUser(App.getCurrentUser());
            }
            return level.getActiveZombies().isEmpty();
        }
        return false;
    }

    @Override
    public void setCurrentLevel(MiniGameLevels currentLevel) {
        this.currentLevel = currentLevel;
    }
}
