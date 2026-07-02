package models.world.loseCondition;

import models.world.GameWorld;

public class DeadLineLose implements LoseCondition{
    private int deadLineCol;

    public DeadLineLose(int deadLineCol){
        this.deadLineCol = deadLineCol;
    }

    @Override
    public boolean checkLose(GameWorld game) {
        return game.getActiveZombies().stream()
                .anyMatch(zombie -> zombie.getY() <= deadLineCol);
    }
}
