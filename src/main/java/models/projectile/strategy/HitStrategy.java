package models.projectile.strategy;

import models.zombie.Zombie;

public interface HitStrategy {
    void applyDamage(Zombie zombie);
}