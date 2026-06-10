package models.miniGame.vaseBreaker;

import models.world.GameWorld;
import models.world.loseCondition.LoseCondition;

public class VaseBreakerLose implements LoseCondition {

    @Override
    public boolean checkLose(GameWorld game) {
        return false;
    }
}
