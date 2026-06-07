package models.world.loseCondition;

import models.world.GameWorld;

public class DeadLineLose implements LoseCondition{
    @Override
    public boolean checkLose(GameWorld game) {
        return false;
    }
}
