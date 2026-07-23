package models.world.obstacles;

import models.plant.Plant;
import models.world.Cell;

public class OctopusObstacle extends Obstacle {
    private Plant targetPlant;
    private Cell cell;

    public OctopusObstacle(float x, float y, Plant targetPlant, Cell cell) {
        super(x, y, 200); // جان اختاپوس ۲۰۰ است
        this.targetPlant = targetPlant;
        this.cell = cell;
        if (targetPlant != null) {
            targetPlant.setDisabled(true); // متوقف کردن کامل گیاه
        }
    }

    @Override
    public boolean blocksProjectiles() {
        return !isDestroyed;
    }

    @Override
    public void takeDamage(int amount, String type) {
        if (isDestroyed) return;
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            die();
        }
    }

    @Override
    public void die() {
        if (targetPlant != null && !targetPlant.isDead()) {
            targetPlant.setDisabled(false); // آزاد شدن گیاه پس از نابودی اختاپوس
        }
        if (cell != null) {
            cell.removeObstacle();
        }
        super.die();
    }
}