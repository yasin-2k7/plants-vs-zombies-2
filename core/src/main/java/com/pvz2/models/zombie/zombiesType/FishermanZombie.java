package com.pvz2.models.zombie.zombiesType;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantLayer;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;

public class FishermanZombie extends Zombie {
    private static final int HOOK_INTERVAL = 45;
    private int hookCooldown;

    public FishermanZombie(int health, int damage) {
        super(Zombies.FISHERMAN, health, 0, damage);
        this.hookCooldown = 0;
    }

    @Override
    public void update() {
        if (isDead) return;

        if (hookCooldown <= 0) {
            tryHook();
            hookCooldown = HOOK_INTERVAL;
        } else {
            hookCooldown--;
        }
    }

    private void tryHook() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;
        int row = (int) (this.y / App.getCellHeight());
        if (row < 0 || row >= game.getRows()) return;
        Plant target = game.getNearestPlantInRow(row, this.x + 10);
        if (target == null) return;
        float targetX = target.getX();
        float targetY = target.getY();
        float distance = Math.abs(targetX - this.x);
        if (distance < App.getCellWidth()) {
            target.die();
            GameMenuController.updateState("Fisherman threw and destroyed plant at (" + targetX + ", " + targetY + ")");
            return;
        }
        Cell currentCell = Cell.findCell(targetX, targetY, game.getGrid());
        if (currentCell == null) return;
        int targetCol = currentCell.getCol() + 1;
        if (targetCol >= game.getCols()) return;
        Cell targetCell = game.getGrid()[row][targetCol];
        if (targetCell == null || !targetCell.isEmpty()) {
            return;
        }
        PlantLayer layer = null;
        for (PlantLayer l : PlantLayer.values()) {
            if (currentCell.getPlant(l) == target) {
                layer = l;
                break;
            }
        }
        if (layer == null) return;
        currentCell.setPlant(null, layer);
        targetCell.setPlant(target, layer);
        target.setX((int) targetCell.getX());
        target.setY((int) targetCell.getY());
        target.setCell(targetCell);
        GameMenuController.updateState("Fisherman pulled plant from (" +
                currentCell.getX() + ", " + currentCell.getY() +
                ") to (" + targetCell.getX() + ", " + targetCell.getY() + ")");
    }

    @Override
    public void move() {
    }
}
