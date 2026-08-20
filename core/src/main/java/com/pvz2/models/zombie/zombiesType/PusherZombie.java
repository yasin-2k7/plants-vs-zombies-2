package com.pvz2.models.zombie.zombiesType;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;

import java.util.Random;

public class PusherZombie extends Zombie {
    private static final float ROW_SWITCH_INTERVAL = 5.0f;
    private String objectName;
    private int objectHealth;
    private float rowSwitchCooldown = 0f;
    private Random random = new Random();

    public PusherZombie(int health, double speed, int damage, String pushedObjectName, int objHealth) {
        super(Zombies.PUSHER, health, speed, damage);
        this.objectName = pushedObjectName;
        this.objectHealth = objHealth;
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        if (objectHealth > 0) {
            int excess = amount - objectHealth;
            if (excess > 0) {
                objectHealth = 0;
                super.takeDamage(excess, damageType);
            } else {
                objectHealth -= amount;
            }
        } else {
            super.takeDamage(amount, damageType);
        }
    }

    @Override
    public void update(float delta) {
        if (isDead) return;
        GameWorld game = App.getCurrentGame();
        if (game == null) {
            super.update(delta);
            return;
        }
        Cell zombieCell = Cell.findZombieCell(game.getGrid(), this);
        boolean crushed = false;
        if (objectHealth > 0 && zombieCell != null) {
            Plant plantHere = zombieCell.getPlant();
            if (plantHere != null && !plantHere.isDead()) {
                plantHere.die();
                crushed = true;
                if ("ICEBLOCK".equals(objectName)) {
                    objectHealth = 0;
                }
                GameMenuController.updateState(
                        objectName + " crushed plant at (" + plantHere.getX() + ", " + plantHere.getY() + ")");
            }
        }
        if ("PIANO".equals(objectName) && objectHealth > 0) {
            if (rowSwitchCooldown <= 0) {
                switchOwnRow(game);
                rowSwitchCooldown = ROW_SWITCH_INTERVAL;
            } else {
                rowSwitchCooldown-= delta;
            }
        }
        if (!crushed) {
            super.update(delta);
        } else {
            this.move(delta);
        }
    }

    @Override
    public String getAnimationClip() {
        if (isDead) return "die";
        if ("PIANO".equals(objectName) && objectHealth > 0) {
            return "play";
        }
        return "idle";
    }

    private void switchOwnRow(GameWorld game) {
        Cell currentCell = Cell.findZombieCell(game.getGrid(), this);
        if (currentCell == null) return;

        int currentRow = currentCell.getRow();
        int targetRow = currentRow + (random.nextBoolean() ? 1 : -1);
        if (targetRow < 0 || targetRow >= game.getRows()) {
            targetRow = currentRow + (random.nextBoolean() ? -1 : 1);
            if (targetRow < 0 || targetRow >= game.getRows()) return;
        }

        this.setY(App.getCellCenterY(targetRow));
        GameMenuController.updateState("Pianist switched to row " + (targetRow + 1));
    }

    public String getObjectName() {
        return objectName;
    }

    public int getObjectHealth() {
        return objectHealth;
    }
}
