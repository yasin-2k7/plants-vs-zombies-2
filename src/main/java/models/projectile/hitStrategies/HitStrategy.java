package models.projectile.hitStrategies;

import models.projectile.Projectile;
import models.zombie.Zombie;

import java.util.List;

public interface HitStrategy {
    void applyDamage(Zombie target, List<Zombie> allZombies, Projectile projectile);
}