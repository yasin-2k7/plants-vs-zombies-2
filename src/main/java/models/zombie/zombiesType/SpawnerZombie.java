package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.world.GameWorld;
import models.zombie.Zombie;
import models.zombie.ZombieFactory;

public class SpawnerZombie extends Zombie {
    private boolean isGargantuar;
    private int spawnCooldown; // زمان بین هر بار احضار
    private int currentCooldown = 0;
    private boolean hasThrownImp;

    public SpawnerZombie(int health, int speed, int damage, boolean isGargantuar) {
        super(Zombies.SPAWNER, health, speed, damage);
        this.isGargantuar = isGargantuar;
        this.spawnCooldown = 30; // 2 ثانیه مثلا
        this.currentCooldown = 0;
        this.hasThrownImp = false;
    }

    @Override
    public void update() {
        if (isDead) return;
        super.update();

        if (isGargantuar) {
            // اگر به نصف جان رسید و هنوز ایمپ پرتاب نشده
            if (!hasThrownImp && this.health <= this.maxHealth / 2) {
                throwImp();
                hasThrownImp = true;
            }
        } else {
            // پادشاه: هر چند ثانیه یک زامبی را شوالیه می‌کند
            if (currentCooldown <= 0) {
                knightNearbyZombie();
                currentCooldown = spawnCooldown;
            } else {
                currentCooldown--;
            }
        }
    }

    private void throwImp() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;
        // ایجاد ایمپ در ستون سوم (سمت چپ)
        Zombie imp = new ZombieFactory().createZombie(Zombies.IMP);
        imp.setX(300); // ستون سوم
        imp.setY(this.y);
        game.getActiveZombies().add(imp);
    }

    private void knightNearbyZombie() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;
        // پیدا کردن یک زامبی معمولی در اطراف (همان سطر، فاصله کم)
        for (Zombie z : game.getActiveZombies()) {
            if (z.getName() == Zombies.ZOMBIE && Math.abs(z.getY() - this.y) < 30) {
                // تبدیل به شوالیه (کلاه‌خود و شانه‌بند)
                // در عمل باید نوع زامبی تغییر کند یا زره اضافه شود
                ((ArmoredZombie) z).stripArmor(); // فرض
                break;
            }
        }
    }
}
