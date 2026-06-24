package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.GameWorld;
import models.zombie.Zombie;

public class FishermanZombie extends Zombie {
    private static final int HOOK_INTERVAL = 45; // 3 ثانیه (15 تیک در ثانیه)
    private int hookCooldown;// زمان انتظار بین هر بار قلاب انداختن

    public FishermanZombie(int health, int damage) {
        super(Zombies.FISHERMAN, health, 0, damage);
        this.hookCooldown = 0;
    }

    @Override
    public void update() {
        if (isDead) return;
        // ماهیگیر حرکت نمی‌کند، فقط قلاب می‌اندازد
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
        // پیدا کردن نزدیک‌ترین گیاه در همان سطر، سمت راست
        Plant target = game.getNearestPlantInRow((int)this.y, this.x + 10);
        if (target == null) return;

        int targetX = target.getX();
        int targetY = target.getY();
        // اگر گیاه در کنار ماهیگیر باشد (فاصله کمتر از یک خانه)
        if (Math.abs(targetX - this.x) < 60) {
            // نابود کردن گیاه
            target.die();
        } else {
            // بررسی اینکه خانه سمت راست گیاه خالی باشد
            if (game.isTileEmpty(targetX + 60, targetY)) {
                // حرکت گیاه یک خانه به سمت راست (نزدیک به ماهیگیر)
                target.setX(targetX + 60);
            }
        }
    }

    @Override
    public void move() {
        // حرکت نمی‌کند
    }
}
