package models.world.loseCondition;

import models.world.GameWorld;

public class TimedWarLose implements LoseCondition{
    private long timeLimit;
    private int targetKills;
    private int currentKills = 0;
    private long startTime;

    public TimedWarLose(long timeLimit, int targetKills){
        this.timeLimit = timeLimit;
        this.targetKills = targetKills;
        this.startTime = System.currentTimeMillis();
    }

    public void onZombieKilled(){
        currentKills++;
    }


    @Override
    public boolean checkLose(GameWorld game) {
        long elapsed = System.currentTimeMillis() - startTime;

        return elapsed >= timeLimit && currentKills < targetKills;
    }
}
