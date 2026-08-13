package com.pvz2.models.zombie.zombiesType;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.Sun;
import com.pvz2.models.zombie.Zombie;

import java.util.List;

public class SunStealerZombie extends Zombie {
    private static final float STEAL_INTERVAL = 3.0f;
    private static final float LASER_DELAY = 7.5f;
    private int stolenSun;
    private boolean isRa;
    private float stealTimer;
    private boolean isStealing;

    public SunStealerZombie(int health, double speed, int damage, boolean isRa) {
        super(Zombies.SUN_STEALER, health, speed, damage);
        this.stolenSun = 0;
        this.isRa = isRa;
        this.stealTimer = 0;
        this.isStealing = false;
    }

    @Override
    public void update(float delta) {
        if (isDead) return;
        super.update(delta);
        GameWorld game = App.getCurrentGame();
        if (game == null) return;
        if (isRa) {
            List<Sun> suns = game.getActiveSuns();
            for (Sun sun : suns) {
                if (sun.isCollected()) continue;
                float dx = sun.getX() - this.x;
                float dy = sun.getY() - this.y;
                if (Math.abs(dx) < 150 && Math.abs(dy) < 150) {
                    sun.collect();
                    stolenSun += sun.getSize();
                    GameMenuController.updateState("Ra stole a sun of size " + sun.getSize());
                }
            }
        } else {
            if (!isStealing) {
                Cell zombieCell = Cell.findZombieCell(game.getGrid(), this);
                if (zombieCell != null) {
                    List<Cell> neighborCells = Cell.getNeighborCells(zombieCell, game.getGrid(), 4);
                    boolean hasPlant = neighborCells.stream().anyMatch(cell -> !cell.isEmpty());
                    if (hasPlant) {
                        isStealing = true;
                        stealTimer = 0f;
                        GameMenuController.updateState("Turquoise started stealing suns!");
                    }
                }
            }

            if (isStealing) {
                stealTimer+= delta;
                if (stealTimer % STEAL_INTERVAL == 0) {
                    int stolen = game.stealSunFromPlayer(25);
                    stolenSun += stolen;
                    GameMenuController.updateState("Turquoise stole 25 suns. Total stolen: " + stolenSun);
                }
                if (stealTimer >= LASER_DELAY) {
                    fireLaser(game);
                    isStealing = false;
                    stealTimer = 0f;
                }
            }
        }
    }

    private void fireLaser(GameWorld game) {
        int row = (int) (this.y / App.getCellHeight());
        int col = (int) (this.x / App.getCellWidth());
        for (int i = 1; i <= 4; i++) {
            int targetCol = col - i;
            if (targetCol < 0) break;
            if (row >= 0 && row < game.getRows() && targetCol < game.getCols()) {
                Cell cell = game.getGrid()[row][targetCol];
                Plant plant = cell.getPlant();
                if (plant != null && !plant.isDead()) {
                    plant.die();
                    GameMenuController.updateState(
                            "Laser destroyed plant at (" + plant.getX() + ", " + plant.getY() + ")");
                }
            }
        }
    }

    @Override
    public void die() {
        GameWorld game = App.getCurrentGame();
        if (game != null) {
            if (isRa) {
                game.addSunToPlayer(stolenSun);
                GameMenuController.updateState("Ra returned " + stolenSun + " suns.");
            } else {
                int returned = stolenSun / 2;
                game.addSunToPlayer(returned);
                GameMenuController.updateState("Turquoise returned " + returned + " suns (half of " + stolenSun + ").");
            }
        }
        super.die();
    }
}
