package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.GameWorld;
import models.zombie.Zombie;

public class RangedZombie extends Zombie {
    private String projectileType;// "SNOWBALL", "OCTOPUS", "BONE"
    private int cooldown;
    private final int COOLDOWN_MAX = 30; // 2 ثانیه

    public RangedZombie(int health, double speed, int damage, String projectileType) {
        super(Zombies.RANGED, health, speed, damage);
        this.projectileType = projectileType;
        this.cooldown = 0;
    }

    public void throwProjectile() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;
        Plant target = game.getNearestPlantInRow((int)this.y, this.x + 10);
        if (target == null) return;

        switch (projectileType) {
            case "SNOWBALL":
                // گیاه را کند می‌کند (یا یخ می‌زند)
                target.applySlow(100); // 100 تیک کندی
                break;
            case "OCTOPUS":
                // گیاه را با اختاپوس می‌پوشاند (غیرفعال)
                target.setDisabled(true);
                break;
            case "BONE":
                game.createGrave(target.getX(), target.getY());
                break;
        }
    }

    @Override
    public void update() {
        if (isDead) return;
        super.update();
        if (cooldown <= 0) {
            throwProjectile();
            cooldown = COOLDOWN_MAX;
        } else {
            cooldown--;
        }
    }
}
