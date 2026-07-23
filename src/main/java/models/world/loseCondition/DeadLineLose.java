package models.world.loseCondition;

import models.world.GameWorld;

public class DeadLineLose implements LoseCondition{
    private int deadLineCol;

    public DeadLineLose(int deadLineCol){
        this.deadLineCol = deadLineCol;
    }

    @Override
    public boolean checkLose(GameWorld game) {
        float deadLineX = deadLineCol * 100f;
        return game.getActiveZombies().stream()
                .anyMatch(zombie -> zombie.getX() <= deadLineX);
    }
}
