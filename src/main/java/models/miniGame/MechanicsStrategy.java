package models.miniGame;

import models.world.GameWorld;

public interface MechanicsStrategy {
    void applyMechanics(GameWorld world);
    default void handleCustomCommand(String command, GameWorld world) {}
}
