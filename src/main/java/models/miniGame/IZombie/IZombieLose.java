package models.miniGame.IZombie;

import models.world.GameWorld;
import models.world.loseCondition.LoseCondition;

public class IZombieLose implements LoseCondition {
    @Override
    public boolean checkLose(GameWorld game) {
        if (game instanceof IZombieLevel level) {

            if (!level.getActiveZombies().isEmpty()) {
                return false;
            }

            return level.getSun() < level.getMinZombieCost();
        }
        return false;
    }
}
