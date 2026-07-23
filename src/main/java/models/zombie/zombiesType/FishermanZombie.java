package models.zombie.zombiesType;

import controller.GameMenuController;
import models.core.App;
import models.enums.PlantLayer;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.zombie.Zombie;

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

        // ماهیگیر حرکت نمی‌کند
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
        int targetCol = currentCell.getCol() + 1; // یک خانه به راست
        if (targetCol >= game.getCols()) return; // اگر خارج از محدوده بود، کاری نکن
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
        target.setCell(targetCell); // به‌روزرسانی مرجع سلول
        GameMenuController.updateState("Fisherman pulled plant from (" + currentCell.getX() + ", " + currentCell.getY() +
                ") to (" + targetCell.getX() + ", " + targetCell.getY() + ")");
    }

    @Override
    public void move() {
        // ماهیگیر حرکت نمی‌کند
    }
}