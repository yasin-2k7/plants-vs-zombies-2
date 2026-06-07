package models.miniGame.vaseBreaker;

import models.miniGame.MechanicsStrategy;
import models.world.GameWorld;

public class VaseBreakerMechanics implements MechanicsStrategy {

    @Override
    public void applyMechanics(GameWorld world) {

    }

    @Override
    public void handleCustomCommand(String command, GameWorld world) {
        MechanicsStrategy.super.handleCustomCommand(command, world);
    }
}

