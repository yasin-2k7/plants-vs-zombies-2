package models.zombie.zombiesType;

import models.core.App;
import models.world.GameWorld;
import models.zombie.Zombie;
import models.enums.Zombies;

public class SunStealerZombie extends Zombie {
    private int stolenSun;
    private boolean isRa;


    public SunStealerZombie(int health, int speed, int damage, boolean isRa) {
        super(Zombies.SUN_STEALER, health, speed, damage);
        this.stolenSun = 0;
        this.isRa = isRa;
    }

    @Override
    public void update() {
        if (isDead) return;
        super.update();
        GameWorld game = App.getCurrentGame();
        if (game != null) {
            if (isRa) {
                int collected = game.collectSunInRadius(this.x, this.y, 80);
                this.stolenSun += collected;
            } else {
                int stolen = game.stealSunFromPlayer(25);
                this.stolenSun += stolen;
            }
        }
    }

    @Override
    public void die() {
        // پس از مرگ، نیمی از خورشیدهای دزدیده شده را پس می‌دهد
        GameWorld game = App.getCurrentGame();
        if (game != null) {
            if (isRa){
                game.addSunToPlayer(this.stolenSun);
            }
            else {
            game.addSunToPlayer(this.stolenSun / 2);
            }
        }
        super.die();
    }
}
