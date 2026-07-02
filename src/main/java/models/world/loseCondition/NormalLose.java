package models.world.loseCondition;

import models.world.GameWorld;

public class NormalLose implements LoseCondition{
    @Override
    public boolean checkLose(GameWorld game) {
        return game.getActiveZombies().stream()
                .anyMatch(zombie -> {
                    if(zombie.getX() < 0){
                        return game.getLawnMowers().stream()
                                .noneMatch(lawnMower -> lawnMower.getRow() == zombie.getX()
                                            && !lawnMower.isActive());
                    }
                    return false;
                });
    }
}
