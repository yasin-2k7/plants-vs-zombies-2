package models.miniGame.beghouled;

import models.world.GameWorld;
import models.world.loseCondition.LoseCondition;

public class BeghouledLose implements LoseCondition {

    @Override
    public boolean checkLose(GameWorld game) {
        return false;
    }
}
