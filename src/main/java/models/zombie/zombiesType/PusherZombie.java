package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PusherZombie extends Zombie {
    private String objectName;
    private int objectHealth;
    private int rowSwitchCooldown = 0;
    private static final int ROW_SWITCH_INTERVAL = 50; // هر ۵۰ تیک یک بار
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
    public void update() {
        if (isDead) return;

        GameWorld game = App.getCurrentGame();
        if (game == null) {
            super.update();
            return;
        }

        Cell zombieCell = Cell.findZombieCell(game.getGrid(), this);
        boolean crushed = false;

        // اگر شیء سالم است، گیاهان را له می‌کند
        if (objectHealth > 0 && zombieCell != null) {
            // ۱. گیاه در سلول خود زامبی
            Plant plantHere = zombieCell.getPlant();
            if (plantHere != null && !plantHere.isDead()) {
                plantHere.die();
                crushed = true;
                // اگر شیء یخ باشد، یخ می‌شکند
                if ("ICEBLOCK".equals(objectName)) {
                    objectHealth = 0;
                }
                System.out.println(objectName + " crushed plant at (" + plantHere.getX() + ", " + plantHere.getY() + ")");
            }

            // ۲. گیاه در سلول جلویی (اگر شیء جلوتر است)
            Cell frontCell = Cell.nextCell(zombieCell, game.getGrid());
            if (frontCell != null) {
                Plant plantFront = frontCell.getPlant();
                if (plantFront != null && !plantFront.isDead()) {
                    plantFront.die();
                    crushed = true;
                    if ("ICEBLOCK".equals(objectName)) {
                        objectHealth = 0;
                    }
                    System.out.println(objectName + " crushed plant at (" + plantFront.getX() + ", " + plantFront.getY() + ")");
                }
            }
        }

        // اگر شیء سالم است و پیانیست است، زامبی‌های همسایه را جابه‌جا کن
        if ("PIANO".equals(objectName) && objectHealth > 0) {
            if (rowSwitchCooldown <= 0) {
                switchNearbyZombies(game);
                rowSwitchCooldown = ROW_SWITCH_INTERVAL;
            } else {
                rowSwitchCooldown--;
            }
        }

        // حرکت یا خوردن (در صورت عدم له شدن)
        if (!crushed) {
            super.update(); // شامل حرکت و خوردن (اگر objectHealth <= 0 باشد)
        } else {
            // فقط حرکت کن، نخور
            this.move();
        }
    }

    private void switchNearbyZombies(GameWorld game) {
        Cell zombieCell = Cell.findZombieCell(game.getGrid(), this);
        if (zombieCell == null) return;

        int currentRow = zombieCell.getRow();
        int targetRow = currentRow + (random.nextBoolean() ? 1 : -1);
        // محدودیت ردیف
        if (targetRow < 0 || targetRow >= game.getRows()) {
            targetRow = currentRow + (random.nextBoolean() ? -1 : 1);
            if (targetRow < 0 || targetRow >= game.getRows()) return;
        }

        // پیدا کردن زامبی‌های همسایه (همان سطر)
        List<Zombie> sameRowZombies = new ArrayList<>();
        for (Zombie z : game.getActiveZombies()) {
            if (z != this && Math.abs(z.getY() - this.y) < 10) {
                sameRowZombies.add(z);
            }
        }
        if (sameRowZombies.isEmpty()) return;

        // انتخاب یک زامبی تصادفی از همان سطر
        Zombie targetZombie = sameRowZombies.get(random.nextInt(sameRowZombies.size()));

        // جابه‌جایی به ردیف هدف
        float newY = targetRow * App.getCellHeight() + App.getCellHeight() / 2;
        targetZombie.setY(newY);
        System.out.println("Pianist switched a zombie to row " + (targetRow + 1));
    }

    public int getObjectHealth() { return objectHealth; }
    public String getObjectName() { return objectName; }
}