package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.zombie.Zombie;

public class ElementalZombie extends Zombie {
    private boolean isExplorer;     // true: explorer, false: prospector
    private boolean isIgnited;      // مشعل روشن / دینامیت فعال
    private int fuseTimer;          // برای پروسپکتور (تعداد تیک تا انفجار)
    private boolean hasExploded;    // برای پروسپکتور: آیا منفجر شده؟

    public ElementalZombie(int health, double speed, int damage, boolean isExplorer) {
        super(Zombies.ELEMENTAL, health, speed, damage);
        this.isExplorer = isExplorer;
        this.isIgnited = true;
        this.fuseTimer = 150;
        this.hasExploded = false;
    }

    @Override
    public void update() {
        if (isDead) return;

        //  پروسپکتور: شمارش معکوس فتیله
        if (!isExplorer && isIgnited && !hasExploded) {
            fuseTimer--;
            if (fuseTimer <= 0) {
                explode();
            }
        }
        super.update();

        if (isExplorer && isIgnited) {
            GameWorld game = App.getCurrentGame();
            if (game != null) {
                Cell currentCell = Cell.findZombieCell(game.getGrid(), this);
                if (currentCell != null) {
                    Cell frontCell = Cell.nextCell(currentCell, game.getGrid());
                    if (frontCell != null) {
                        Plant plant = frontCell.getPlant();
                        if (plant != null && !plant.isDead()) {
                            plant.die();
                            System.out.println("Explorer burned plant at (" + plant.getX() + ", " + plant.getY() + ")");
                        }
                    }
                }
            }
        }
    }

    private void explode() {
        this.hasExploded = true;
        this.isIgnited = false;
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        // انتقال به انتهای سطر (راست‌ترین ستون)
        int cols = game.getCols();
        float newX = cols * App.getCellWidth() - App.getCellWidth() / 2;
        this.x = newX;
        // حرکت به چپ (سرعت مثبت)
        this.speed = Math.abs(this.speed);
        System.out.println("Prospector exploded and teleported to the right end of the row.");
    }

    // خاموش کردن آتش (تیر یخی)
    public void extinguish() {
        this.isIgnited = false;
        if (!isExplorer) {
            System.out.println("Prospector's dynamite extinguished.");
        } else {
            System.out.println("Explorer's torch extinguished.");
        }
    }

    // روشن کردن آتش (تیر آتشین) - فقط برای مشعل‌دار
    public void ignite() {
        if (isExplorer) {
            this.isIgnited = true;
            System.out.println("Explorer's torch ignited.");
        }
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        if ("ICE".equals(damageType)) {
            extinguish();
        } else if ("FIRE".equals(damageType) && isExplorer) {
            ignite();
        }
        super.takeDamage(amount, damageType);
    }

    public boolean isExplorer() { return isExplorer; }
    public boolean isIgnited() { return isIgnited; }
    public boolean hasExploded() { return hasExploded; }
}