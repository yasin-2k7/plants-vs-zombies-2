package models.world.winCondition;

import models.core.App;
import models.core.UserDataManager;
import models.miniGame.MiniGameLevels;
import models.world.GameWorld;
import models.world.mechanics.NormalMechanic;

public class NormalWin implements WinCondition {
    MiniGameLevels currentLevel;

    @Override
    public boolean checkWin(GameWorld game) {
        NormalMechanic mechanic = game.getMechanic(NormalMechanic.class);
        if (mechanic.getWaveManager().isLevelCompleted() && game.getActiveZombies().isEmpty()) {
            if (currentLevel != null){
                App.getCurrentUser().getMiniGameLevels().add(currentLevel);
                if (currentLevel.level != 3) App.getCurrentUser().notifyMinigameUnlocked(
                        currentLevel.miniGame.name() + " " + (currentLevel.level+1));
            }

            UserDataManager.saveUser(App.getCurrentUser());
        }

        return mechanic.getWaveManager().isLevelCompleted() &&
                game.getActiveZombies().isEmpty();
    }

    @Override
    public void setCurrentLevel(MiniGameLevels currentLevel) {
        this.currentLevel = currentLevel;
    }
}
