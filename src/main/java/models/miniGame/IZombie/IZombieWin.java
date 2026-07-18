package models.miniGame.IZombie;

import models.world.GameWorld;
import models.world.winCondition.WinCondition;

public class IZombieWin implements WinCondition {
    @Override
    public boolean checkWin(GameWorld game) {
        if (game instanceof IZombieLevel level) {
            if (level.getBrains().isEmpty()) return false;
            for (Brain brain : level.getBrains()) {
                if (!brain.isEaten()) return false;
            }
            return true;
        }
        return false;
    }
}
