package com.pvz2.models.projectile.movementStrategies;

import com.pvz2.models.projectile.Projectile;

public class StationaryMovementStrategy implements MovementStrategy {
    private final float lifespan;
    private float elapsed = 0f;

    public StationaryMovementStrategy(float lifespan) {
        this.lifespan = lifespan;
    }

    @Override
    public float changeOriginY() {
        return 0;
    }

    @Override
    public void move(Projectile projectile, float delta) {
        elapsed += delta; // x/y deliberately untouched
    }

    @Override
    public boolean isDead(Projectile projectile) {
        return elapsed >= lifespan;
    }
}
