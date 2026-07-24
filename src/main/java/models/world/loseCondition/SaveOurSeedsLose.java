package models.world.loseCondition;

import models.world.GameWorld;

public class SaveOurSeedsLose implements LoseCondition {
    private boolean protectedPlantEaten = false;

    public void onProtectedPlantEaten() {
        protectedPlantEaten = true;
    }

    @Override
    public boolean checkLose(GameWorld game) {
        return protectedPlantEaten;
    }
}
