package models.world.loseCondition;

import models.world.GameWorld;

public class LoveYourPlantsLose implements LoseCondition{
    @Override
    public boolean checkLose(GameWorld game) {
        return false;
    }
}
