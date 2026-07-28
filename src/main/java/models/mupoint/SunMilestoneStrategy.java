package models.mupoint;

import models.core.App;

public class SunMilestoneStrategy implements ScoreStrategy {
    private boolean hasAwarded = false;
    @Override
    public int calculatePoints(KillEvent event) {
        if(!hasAwarded && App.getCurrentGame().getSun() >= 500){
            hasAwarded = true;
            return 100;
        }
        return 0;
    }
}