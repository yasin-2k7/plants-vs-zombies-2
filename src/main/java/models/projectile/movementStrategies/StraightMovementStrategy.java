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
        projectile.setX(projectile.getX() + (speedX * 0.1f));
        projectile.setY(projectile.getY() + (speedY * 0.1f));
        // delta in the future...
    }
}
