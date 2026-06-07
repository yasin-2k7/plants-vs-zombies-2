package models.world.loseCondition;

import models.world.GameWorld;

public class SaveOurSeedsLose implements LoseCondition{
    @Override
    public boolean checkLose(GameWorld game) {
        return false;
    }
}
