package models.world.winCondition;

import models.core.App;
import models.core.UserDataManager;
import models.miniGame.MiniGameLevels;
import models.world.GameWorld;
import models.world.loseCondition.TimedWarLose;

public class TimedWarWin implements WinCondition {
    private final TimedWarLose loseCondition;

    public TimedWarWin(TimedWarLose loseCondition) {
        this.loseCondition = loseCondition;
    }

    @Override
    public boolean checkWin(GameWorld game) {
        if (loseCondition.getCurrentKills() >= loseCondition.getTargetKills()) {
            UserDataManager.saveUser(App.getCurrentUser());
        }
        return loseCondition.getCurrentKills() >= loseCondition.getTargetKills();
    }

    @Override
    public void setCurrentLevel(MiniGameLevels currentLevel) {

    }
}
