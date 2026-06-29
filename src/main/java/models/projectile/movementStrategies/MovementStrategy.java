package models.projectile.movementStrategies;

import models.projectile.Projectile;

public interface MovementStrategy {
    float changeOriginY();
    void move(Projectile projectile);
    boolean isDead(Projectile projectile);
}
