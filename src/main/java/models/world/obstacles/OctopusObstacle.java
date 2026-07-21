package models.world.obstacles;

import models.plant.Plant;
import models.world.Cell;

public class OctopusObstacle extends Obstacle {
    private Plant targetPlant;
    private Cell cell;

    public OctopusObstacle(float x, float y, Plant targetPlant, Cell cell) {
        super(x, y, 200);
        this.targetPlant = targetPlant;
        this.cell = cell;
        if (targetPlant != null) {
            targetPlant.setDisabled(true);
        }
    }

    @Override
    public boolean blocksProjectiles() {
        return !isDestroyed;
    }

    @Override
    public void die() {
        if (targetPlant != null && !targetPlant.isDead()) {
            targetPlant.setDisabled(false);
        }
        if (cell != null) {
            cell.removeObstacle();
        }
        super.die();
    }
}