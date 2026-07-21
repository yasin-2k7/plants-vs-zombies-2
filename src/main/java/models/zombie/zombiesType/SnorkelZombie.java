package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.zombie.Zombie;

public class SnorkelZombie extends Zombie {
    private boolean isSubmerged;
    private boolean isEating; // برای جلوگیری از بازگشت به زیر آب هنگام خوردن

    public SnorkelZombie(int health, double speed, int damage) {
        super(Zombies.SNORKEL, health, speed, damage);
        this.isSubmerged = true;
        this.isEating = false;
    }

    @Override
    public void update() {
        if (isDead) return;

        GameWorld game = App.getCurrentGame();
        if (game == null) {
            super.update();
            return;
        }

        // ۱. تشخیص سلول فعلی و وضعیت آب
        Cell currentCell = Cell.findZombieCell(game.getGrid(), this);
        boolean inWater = (currentCell != null && currentCell.isWater());

        // ۲. به‌روزرسانی وضعیت زیرآب بودن (در صورت عدم خوردن)
        if (!isEating) {
            if (inWater) {
                isSubmerged = true;
            } else {
                isSubmerged = false;
            }
        }

        // ۳. اگر زیر آب است و به گیاه رسیده، برای خوردن بیرون بیاید
        if (isSubmerged) {
            // گیاه در سلول جلویی (سمت چپ زامبی)
            Cell frontCell = Cell.nextCell(currentCell, game.getGrid());
            if (frontCell != null) {
                Plant plant = frontCell.getPlant();
                if (plant != null && !plant.isDead()) {
                    emergeToEat();
                    isEating = true;
                }
            }
        } else {
            // ۴. اگر در حال خوردن است و گیاه تمام شد، وضعیت را به‌روز کن
            if (isEating) {
                Cell frontCell = Cell.nextCell(currentCell, game.getGrid());
                Plant plant = (frontCell != null) ? frontCell.getPlant() : null;
                if (plant == null || plant.isDead()) {
                    isEating = false;
                    // اگر در آب است، دوباره زیر آب برو
                    if (inWater) {
                        isSubmerged = true;
                    }
                }
            }
        }
        super.update();
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        if (isSubmerged && !"LOBBER".equals(damageType)) {
            return;
        }
        super.takeDamage(amount, damageType);
    }

    public void emergeToEat() {
        this.isSubmerged = false;
    }

    public void submerge() {
        if (!isEating) {
            this.isSubmerged = true;
        }
    }

    public boolean isSubmerged() { return isSubmerged; }
}