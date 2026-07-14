package models.projectile.hitStrategies;

import models.Damageable;
import models.projectile.Projectile;
import models.zombie.Zombie;

import java.util.List;

public interface HitStrategy {
    void applyDamage(Damageable target, List<Damageable> allTargets, Projectile projectile);
}