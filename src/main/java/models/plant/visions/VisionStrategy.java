package models.plant.visions;

import models.Damageable;
import models.plant.Plant;
import models.projectile.Projectile;

public interface VisionStrategy {
    Damageable findZombie(Plant owner);
    static boolean isBetween(float number, float a, float b){
        return number >= Math.min(a, b) && number <= Math.max(a, b);
    }

    boolean isOutOfRange(Projectile projectile);
}
