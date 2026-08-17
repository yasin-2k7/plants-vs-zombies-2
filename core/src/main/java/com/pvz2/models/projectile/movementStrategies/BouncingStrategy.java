package com.pvz2.models.projectile.movementStrategies;

import com.pvz2.models.core.App;
import com.pvz2.models.projectile.Projectile;

public class BouncingStrategy implements MovementStrategy {
    private final int maxBounces;
    private float speedX;
    private float speedY;
    private int bounceCount = 0;

    public BouncingStrategy(float speedX, float speedY, int maxBounces) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.maxBounces = maxBounces;
    }

    @Override
    public float changeOriginY() {
        return 0;
    }

    @Override
    public void move(Projectile projectile, float delta) {
        if (projectile.getX() <= App.getFirstCellX() && speedX < 0) {
            speedX = -speedX;
            bounceCount++;
            projectile.getLastTarget().clear();
        } else if (projectile.getX() >= App.getFirstCellX()+9*App.getCellWidth() && speedX > 0) {
            speedX = -speedX;
            bounceCount++;
            projectile.getLastTarget().clear();
        }

        float bottomLimit = App.getFirstCellY();
        float topLimit = App.getFirstCellY() + App.getCellHeight() * 5;

        if (projectile.getY() <= bottomLimit && speedY < 0) {
            speedY = -speedY;
            bounceCount++;
            projectile.getLastTarget().clear();
        } else if (projectile.getY() >= topLimit && speedY > 0) {
            speedY = -speedY;
            bounceCount++;
            projectile.getLastTarget().clear();
        }

        projectile.setX(projectile.getX() + (speedX * delta));
        projectile.setY(projectile.getY() + (speedY * delta));
    }



    @Override
    public boolean isDead(Projectile projectile) {
        return bounceCount > maxBounces;
    }
}
