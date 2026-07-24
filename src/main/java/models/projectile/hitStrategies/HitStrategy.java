package models.projectile.hitStrategies;

import models.Damageable;
import models.plant.Plant;
import models.projectile.Projectile;

import java.util.List;

public interface HitStrategy {
    void applyDamage(Damageable target, List<Damageable> allTargets, Projectile projectile);
    String getElement();
    int getDamage();
    void setElement(String element);
    void increaseDamage(int factor);
    void applyDamage(Plant plant, Projectile projectile);
}