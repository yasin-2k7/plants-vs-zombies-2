package com.pvz2.models.lawnMower;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.zombie.Zombie;

import java.util.List;

public class LawnMower {
    private int row;
    private boolean isActive;
    private boolean isSpent;
    private double positionX;
    private double speed = 150.0;
    private boolean isAlive = true;

    public LawnMower(int row) {
        this.row = row;
        this.isActive = false;
        this.isSpent = false;
        this.positionX = 0.0;
    }

    public void activate() {
        if (!isActive && !isSpent) {
            this.isActive = true;
            this.isAlive = true;
            GameMenuController.updateState(
                    "The lawn mower in the row " + row + "is triggered and killed these zombies:");
        }
    }

    public void checkCollision(Zombie firstZombieInRow) {
        if (firstZombieInRow != null && firstZombieInRow.getX() <= 0) {
            if (!isActive && !isSpent) {
                activate();
            }
        }
    }

    public void mowZombies(List<Zombie> zombiesInRow) {
        if (!isActive) return;

        for (Zombie z : zombiesInRow) {
            if (!z.isDead() && z.getX() <= this.positionX) {
                if (!z.isBoss()) {
                    z.setKiller(null);
                    z.takeDamage(99999, "MOWER");

                    User user = App.getCurrentUser();
                    if (user != null) {
                        user.getQuestStats().incrementLawnmowerKills();
                        user.getQuestManager().checkAllQuests(user);
                    }
                }
            }
        }
    }

    public void move() {
        if (isActive) {
            positionX += speed;
            if (isOutOfBounds()) {
                isAlive = false;
                isActive = false;
                isSpent = true;
            }
        }
    }

    public boolean isOutOfBounds() {
        return positionX > 1000.0;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getRow() {
        return row;
    }

    public boolean isSpent() {
        return isSpent;
    }
}
