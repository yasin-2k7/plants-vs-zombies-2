package models.miniGame.beghouled;

import models.core.App;
import models.core.UserDataManager;
import models.miniGame.MiniGameLevels;
import models.world.GameWorld;
import models.world.winCondition.WinCondition;

public class BeghouledWinCondition implements WinCondition {
    MiniGameLevels currentLevel;

    @Override
    public boolean checkWin(GameWorld game) {
        BeghouledMechanics mechanics = game.getMechanic(BeghouledMechanics.class);
        if (mechanics == null) return false;
        if (mechanics.getScore() >= mechanics.getTargetScore()){
            if (currentLevel != null) App.getCurrentUser().getMiniGameLevels().add(currentLevel);
            UserDataManager.saveUser(App.getCurrentUser());
        }
        return mechanics.getScore() >= mechanics.getTargetScore();
    }

    @Override
    public void setCurrentLevel(MiniGameLevels currentLevel) {
        this.currentLevel = currentLevel;
    }
}