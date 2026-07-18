package models.miniGame.vaseBreaker;

import models.world.GameWorld;
import models.world.winCondition.WinCondition;

public class VaseBreakerWinCondition implements WinCondition {

    @Override
    public boolean checkWin(GameWorld gameWorld) {
        if (gameWorld instanceof VaseBreakerLevel level) {

            for (Vase vase : level.getVases()) {
                if (!vase.isBroken()) {
                    return false;
                }
            }
            return level.getActiveZombies().isEmpty();
        }
        return false;
    }
}
