package models.plant.visions;

import models.plant.Plant;
import models.projectile.Projectile;
import models.zombie.Zombie;

public interface VisionStrategy {
    Zombie findZombie(Plant owner);
    static boolean isBetween(float number, float a, float b){
        return number >= Math.min(a, b) && number <= Math.max(a, b);
    }

    boolean isOutOfRange(Projectile projectile);
}
