package models.world.loseCondition;

import models.world.GameWorld;

public class LoveYourPlantsLose implements LoseCondition{
    private int maxLosses;
    private int currentLosses = 0;

    public LoveYourPlantsLose(int maxLosses){
        this.maxLosses = maxLosses;
    }

    public void onPlantEaten(){
        currentLosses++;
    }

    @Override
    public boolean checkLose(GameWorld game) {
        return currentLosses >= maxLosses;
    }
}
