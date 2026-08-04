package com.pvz2.models.projectile.movementStrategies;

import com.pvz2.models.projectile.Projectile;

public class LobbedMovementStrategy implements MovementStrategy {
    private final float maxArcHeight = 150.0f;
    private final float speed = 5f;
    private float t = 0f;

    @Override
    public float changeOriginY() {
        return 0;
    }

    @Override
    public void move(Projectile projectile) {
        float nextX = projectile.getX() + speed * 12;
        projectile.setX(nextX);

        float totalXDistance = projectile.getTargetX() - projectile.getOriginX();
        float currentXDistance = projectile.getX() - projectile.getOriginX();

        t = currentXDistance / totalXDistance;

        if (t >= 1.0f) {
            t = 1.0f;
        }

        float linearY = projectile.getOriginY() + t * (projectile.getTargetY() - projectile.getOriginY());

        float arcY = (float) (4 * maxArcHeight * t * (1.0 - t));

        projectile.setY(linearY + arcY);
    }

    @Override
    public boolean isDead(Projectile projectile) {
        return t == 1.0;
    }
}
