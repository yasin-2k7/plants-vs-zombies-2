package models.zombie.zombiesType;

import controller.GameMenuController;
import models.core.App;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.world.Sun;
import models.zombie.Zombie;
import models.enums.Zombies;

import java.util.List;

public class SunStealerZombie extends Zombie {
    private int stolenSun;
    private boolean isRa;
    private int stealTimer;
    private boolean isStealing;
    private static final int STEAL_INTERVAL = 15;  // 1 second (15 ticks)
    private static final int LASER_DELAY = 75;     // 5 seconds

    public SunStealerZombie(int health, double speed, int damage, boolean isRa) {
        super(Zombies.SUN_STEALER, health, speed, damage);
        this.stolenSun = 0;
        this.isRa = isRa;
        this.stealTimer = 0;
        this.isStealing = false;
    }

    @Override
    public void update() {
        if (isDead) return;
        super.update(); // حرکت و خوردن معمولی

        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        if (isRa) {
            // Ra: جذب خورشیدهای روی زمین در شعاع ۱۵۰
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
            // Turquoise: بررسی وجود گیاه در شعاع ۴ خانه
            if (!isStealing) {
                Cell zombieCell = Cell.findZombieCell(game.getGrid(), this);
                if (zombieCell != null) {
                    List<Cell> neighborCells = Cell.getNeighborCells(zombieCell, game.getGrid(), 4);
                    boolean hasPlant = neighborCells.stream().anyMatch(cell -> !cell.isEmpty());
                    if (hasPlant) {
                        isStealing = true;
                        stealTimer = 0;
                        GameMenuController.updateState("Turquoise started stealing suns!");
                    }
                }
            }

            if (isStealing) {
                stealTimer++;
                // هر ثانیه یکبار دزدی
                if (stealTimer % STEAL_INTERVAL == 0) {
                    int stolen = game.stealSunFromPlayer(25);
                    stolenSun += stolen;
                    GameMenuController.updateState("Turquoise stole 25 suns. Total stolen: " + stolenSun);
                }

                // بعد از ۵ ثانیه شلیک لیزر
                if (stealTimer >= LASER_DELAY) {
                    fireLaser(game);
                    isStealing = false;
                    stealTimer = 0;
                }
            }
        }
    }

    private void fireLaser(GameWorld game) {
        int row = (int) (this.y / App.getCellHeight());
        int col = (int) (this.x / App.getCellWidth());
        // ۴ خانه جلوتر (سمت چپ)
        for (int i = 1; i <= 4; i++) {
            int targetCol = col - i;
            if (targetCol < 0) break;
            if (row >= 0 && row < game.getRows() && targetCol < game.getCols()) {
                Cell cell = game.getGrid()[row][targetCol];
                Plant plant = cell.getPlant();
                if (plant != null && !plant.isDead()) {
                    plant.die();
                    GameMenuController.updateState("Laser destroyed plant at (" + plant.getX() + ", " + plant.getY() + ")");
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