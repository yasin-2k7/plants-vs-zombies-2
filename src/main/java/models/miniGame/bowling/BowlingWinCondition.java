package models.miniGame.bowling;

import models.world.GameWorld;
import models.world.winCondition.WinCondition;

public class BowlingWinCondition implements WinCondition {

    @Override
    public boolean checkWin(GameWorld world) {
        return false;
    }
}
