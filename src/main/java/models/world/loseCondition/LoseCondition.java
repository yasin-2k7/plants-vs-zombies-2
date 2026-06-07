package models.world.loseCondition;

import models.world.GameWorld;

public interface LoseCondition {
    boolean checkLose (GameWorld game);
}
