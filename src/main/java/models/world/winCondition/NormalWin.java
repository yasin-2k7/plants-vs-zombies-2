package models.world.winCondition;

import models.world.GameWorld;
import models.world.mechanics.NormalMechanic;

public class NormalWin implements WinCondition{
    @Override
    public boolean checkWin(GameWorld game) {
        NormalMechanic mechanic = game.getMechanic(NormalMechanic.class);

        return mechanic.getWaveManager().isLevelCompleted() &&
                game.getActiveZombies().isEmpty();
    }
}
