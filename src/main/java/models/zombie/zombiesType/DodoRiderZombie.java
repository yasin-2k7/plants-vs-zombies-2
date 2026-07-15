package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.GameWorld;
import models.zombie.Zombie;

public class DodoRiderZombie extends Zombie {
    private boolean isRiding;

    public DodoRiderZombie(int health, double speed, int damage) {
        super(Zombies.DODO_RIDER, health, speed, damage);
        this.isRiding = true;
    }

    @Override
    public void update() {
        if (isDead) return;
        if (isRiding) {
            // بررسی گیاه جلویی
            GameWorld game = App.getCurrentGame();
            if (game != null) {
                Plant obstacle = game.getPlantAtPosition(this.x - 20, this.y);
                if (obstacle != null && !obstacle.isDead()) {
                    handleObstacle(obstacle);
                }
            }
        }
        super.update(); // حرکت یا خوردن
    }

    private void handleObstacle(Plant plant) {
        if (plant.getType().name().equalsIgnoreCase("TALL_NUT")) {
            return;
        }
        // گیاهانی که از روی آنها می‌پرد (گردو، مین سیب‌زمینی، ...)
        if (plant.getType().name().equalsIgnoreCase("WALL_NUT") ||
                plant.getType().name().equalsIgnoreCase("POTATO_MINE") ||
                plant.getType().name().equalsIgnoreCase("SPIKEWEED")) {
            // پرش به جلو
            this.x -= 120; // یک خانه جلوتر مثلا
        }
    }

    @Override
    public void takeDamage(int damageAmount, String damageType) {
        if (isDead) return;
        // در فصل یخ، کند نمی‌شود (اما آسیب می‌بیند)
        super.takeDamage(damageAmount, damageType);
        // اگر جان کمتر از نصف شود، پرنده از بین می‌رود و پیاده می‌شود
        if (this.health < this.maxHealth / 2 && isRiding) {
            isRiding = false;
            this.speed = (int)(this.speed * 0.6); // کمی کندتر
        }
    }
}
