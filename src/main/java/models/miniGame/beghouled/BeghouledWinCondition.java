package models.miniGame.beghouled;

import models.world.GameWorld;
import models.world.winCondition.WinCondition;

public class BeghouledWinCondition implements WinCondition {
    @Override
    public boolean checkWin(GameWorld game) {
        BeghouledMechanics mechanics = game.getMechanic(BeghouledMechanics.class);
        if (mechanics == null) return false;
        return mechanics.getScore() >= mechanics.getTargetScore();
    }
}