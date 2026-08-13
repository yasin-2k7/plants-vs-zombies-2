package com.pvz2.models.zombie.zombiesType;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;
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
            Cell frontCell = Cell.nextCell(zombieCell, game.getGrid());
            if (frontCell != null) {
                Plant plantFront = frontCell.getPlant();
                if (plantFront != null && !plantFront.isDead()) {
                    plantFront.die();
                    crushed = true;
                    if ("ICEBLOCK".equals(objectName)) {
                        objectHealth = 0;
                    }
                    GameMenuController.updateState(
                            objectName + " crushed plant at (" + plantFront.getX() + ", " + plantFront.getY() + ")");
                }
            }
        }
        if ("PIANO".equals(objectName) && objectHealth > 0) {
            if (rowSwitchCooldown <= 0) {
                switchNearbyZombies(game);
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

    private void switchNearbyZombies(GameWorld game) {
        Cell zombieCell = Cell.findZombieCell(game.getGrid(), this);
        if (zombieCell == null) return;

        int currentRow = zombieCell.getRow();
        int targetRow = currentRow + (random.nextBoolean() ? 1 : -1);
        if (targetRow < 0 || targetRow >= game.getRows()) {
            targetRow = currentRow + (random.nextBoolean() ? -1 : 1);
            if (targetRow < 0 || targetRow >= game.getRows()) return;
        }

        List<Zombie> sameRowZombies = new ArrayList<>();
        for (Zombie z : game.getActiveZombies()) {
            if (z != this && Math.abs(z.getY() - this.y) < 10) {
                sameRowZombies.add(z);
            }
        }
        if (sameRowZombies.isEmpty()) return;

        Zombie targetZombie = sameRowZombies.get(random.nextInt(sameRowZombies.size()));

        float newY = targetRow * App.getCellHeight() + App.getCellHeight() / 2;
        targetZombie.setY(newY);
        GameMenuController.updateState("Pianist switched a zombie to row " + (targetRow + 1));
    }

}
