package models.miniGame.beghouled;

import models.miniGame.MechanicsStrategy;
import models.world.GameWorld;

public class BeghouledMechanics implements MechanicsStrategy {

    @Override
    public void applyMechanics(GameWorld world) {

    }

    @Override
    public void handleCustomCommand(String command, GameWorld world) {
        MechanicsStrategy.super.handleCustomCommand(command, world);
    }
}
