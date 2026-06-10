package models.miniGame.bowling;

import models.world.GameWorld;
import models.world.loseCondition.LoseCondition;

public class BowlingLose implements LoseCondition {

    @Override
    public boolean checkLose(GameWorld game) {
        return false;
    }
}
