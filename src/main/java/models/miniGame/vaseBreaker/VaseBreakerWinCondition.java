package models.miniGame.vaseBreaker;

import models.world.GameWorld;
import models.world.winCondition.WinCondition;

public class VaseBreakerWinCondition implements WinCondition {

    @Override
    public boolean checkWin(GameWorld world) {
        return false;
    }
}
