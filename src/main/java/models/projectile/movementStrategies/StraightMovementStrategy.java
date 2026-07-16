package models.projectile.movementStrategies;

import models.projectile.Projectile;

public class StraightMovementStrategy implements MovementStrategy{
    float speedX;
    float speedY;
    float changeYAmount = 0;

    public StraightMovementStrategy(float speedX, float speedY, float changeYAmount) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.changeYAmount = changeYAmount;
    }

    @Override
    public float changeOriginY() {
        return changeYAmount;
    }

    @Override
    public void move(Projectile projectile) {
        projectile.setX(projectile.getX() + (speedX * 10));
        projectile.setY(projectile.getY() + (speedY * 10));
        // delta in the future...
    }

    @Override
    public boolean isDead(Projectile projectile) {
        return projectile.getX() > 1000 || projectile.getX() < 0 || projectile.getY() > 1000 || projectile.getY() < 0;
    }


}
