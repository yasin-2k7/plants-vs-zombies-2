package models.world.loseCondition;

import models.world.GameWorld;

public class TimedWarLose implements LoseCondition {
    private long timeLimit;
    private int targetKills;
    private int currentKills = 0;
    private long startTime = -1;

    public TimedWarLose(long timeLimit, int targetKills) {
        this.timeLimit = timeLimit;
        this.targetKills = targetKills;
    }

    public void onZombieKilled() {
        currentKills++;
    }


    @Override
    public boolean checkLose(GameWorld game) {
        if (startTime == -1) {
            startTime = game.getCurrentTick();
        }
        long elapsed = game.getCurrentTick() - startTime;

        return elapsed >= timeLimit && currentKills < targetKills;
    }

    public int getCurrentKills() {
        return currentKills;
    }

    public int getTargetKills() {
        return targetKills;
    }

}
