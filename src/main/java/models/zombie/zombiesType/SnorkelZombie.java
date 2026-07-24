package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.world.Cell;
import models.world.GameWorld;
import models.zombie.Zombie;
import models.zombie.state.EatingState;

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

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;

        GameWorld game = App.getCurrentGame();
        if (game != null) {
            int col = (int) (this.x / App.getCellWidth());
            int row = (int) (this.y / App.getCellHeight());
            boolean inWater = false;
            if (row >= 0 && row < game.getRows() && col >= 0 && col < game.getCols()) {
                Cell cell = game.getGrid()[row][col];
                if (cell != null) {
                    inWater = cell.isWater();
                }
            }
            if (inWater && !(this.getCurrentState() instanceof EatingState)) {
                if (!"LOBBER".equalsIgnoreCase(damageType)) {
                    return;
                }
            }
        }

        super.takeDamage(amount, damageType);
    }

    public boolean isUnderwater() {
        return underwater;
    }
}