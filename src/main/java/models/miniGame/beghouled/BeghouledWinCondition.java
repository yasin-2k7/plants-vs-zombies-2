package models.miniGame.beghouled;

import models.world.GameWorld;
import models.world.winCondition.WinCondition;

public class BeghouledWinCondition implements WinCondition {

    @Override
    public boolean checkWin(GameWorld world) {
        return false;
    }
}
