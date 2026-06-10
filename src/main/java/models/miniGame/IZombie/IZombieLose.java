package models.miniGame.IZombie;

import models.world.GameWorld;
import models.world.loseCondition.LoseCondition;

public class IZombieLose implements LoseCondition {
    @Override
    public boolean checkLose(GameWorld game) {
        return false;
    }
}
