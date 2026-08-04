package com.pvz2.models.zombie.zombiesType;

import com.pvz2.models.core.App;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.state.EatingState;

public class SnorkelZombie extends Zombie {
    private boolean underwater;

    public SnorkelZombie(int health, double speed, int damage) {
        super(Zombies.SNORKEL, health, speed, damage);
        this.underwater = false;
    }

    @Override
    public void update() {
        if (isDead) return;

        GameWorld game = App.getCurrentGame();
        if (game == null) {
            super.update();
            return;
        }

        int col = (int) (this.x / App.getCellWidth());
        int row = (int) (this.y / App.getCellHeight());
        boolean inWater = false;
        if (row >= 0 && row < game.getRows() && col >= 0 && col < game.getCols()) {
            Cell cell = game.getGrid()[row][col];
            if (cell != null) {
                inWater = cell.isWater();
            }
        }

        if (this.getCurrentState() instanceof EatingState) {
            underwater = false;
        } else {
            underwater = inWater;
        }

        super.update();
    }

    public boolean isUnderwater() {
        return underwater;
    }
}
