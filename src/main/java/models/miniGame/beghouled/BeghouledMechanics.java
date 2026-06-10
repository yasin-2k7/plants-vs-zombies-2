package models.miniGame.beghouled;

import models.miniGame.MechanicsStrategy;
import models.world.GameWorld;

import java.util.List;

public class BeghouledMechanics implements MechanicsStrategy {
    private List<PlantUpgrade> availableUpgrades;
    @Override
    public void applyMechanics(GameWorld world) {

    }

    @Override
    public void handleCustomCommand(String command, GameWorld world) {
        MechanicsStrategy.super.handleCustomCommand(command, world);
    }
}
