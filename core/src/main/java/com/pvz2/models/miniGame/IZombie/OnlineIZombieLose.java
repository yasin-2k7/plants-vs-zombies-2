package com.pvz2.models.miniGame.IZombie;

import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.loseCondition.LoseCondition;

/**
 * Networked "I, Zombie" lose condition (named from the plants' perspective, matching the
 * WinCondition/LoseCondition split the engine already uses): zombies win the instant every
 * goal brain has been eaten, whether or not the 2-minute timer has run out yet.
 */
public class OnlineIZombieLose implements LoseCondition {
    @Override
    public boolean checkLose(GameWorld game) {
        if (!(game instanceof OnlineIZombieLevel level)) return false;
        if (level.getBrains().isEmpty()) return false;

        for (Brain brain : level.getBrains()) {
            if (!brain.isEaten()) return false;
        }
        return true; // every brain eaten — zombies win
    }
}
