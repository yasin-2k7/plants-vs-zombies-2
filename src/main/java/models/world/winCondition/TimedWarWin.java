package models.world.winCondition;

import models.world.GameWorld;
import models.world.loseCondition.TimedWarLose;

public class TimedWarWin implements WinCondition{
    private final TimedWarLose loseCondition;

    public TimedWarWin(TimedWarLose loseCondition) {
        this.loseCondition = loseCondition;
    }

    @Override
    public boolean checkWin(GameWorld game) {
        return loseCondition.getCurrentKills() >= loseCondition.getTargetKills();
    }
}
