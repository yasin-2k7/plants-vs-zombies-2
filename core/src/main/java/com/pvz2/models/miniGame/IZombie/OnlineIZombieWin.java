package com.pvz2.models.miniGame.IZombie;

import com.pvz2.models.miniGame.MiniGameLevels;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.winCondition.WinCondition;

/**
 * Networked "I, Zombie" win condition: plants win if the timer runs out while at least
 * one goal brain is still uneaten. Mirrors IZombieWin's shape, opposite side, since this
 * world is shared by both players rather than being one user's single-player session.
 */
public class OnlineIZombieWin implements WinCondition {
    public static final float TIME_LIMIT_SECONDS = 180f;

    @Override
    public boolean checkWin(GameWorld game) {
        if (!(game instanceof OnlineIZombieLevel level)) return false;
        if (level.getElapsedTime() < TIME_LIMIT_SECONDS) return false;

        for (Brain brain : level.getBrains()) {
            if (!brain.isEaten()) return true; // time's up, this one survived — plants win
        }
        return false; // time's up, but every brain was already eaten — that's a zombie win, not this
    }

    // Required by WinCondition; unused here since online matches aren't part of a
    // single-player MiniGameLevels progression like the offline IZombieWin's is.
    @Override
    public void setCurrentLevel(MiniGameLevels currentLevel) {}
}
