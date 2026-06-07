package models.miniGame;

import models.world.GameWorld;

public interface WinCondition {
    boolean isWon(GameWorld world);
}
