package models.projectile.movementStrategies;

import models.core.App;
import models.projectile.Projectile;

public class BouncingStrategy implements MovementStrategy{
    private float speedX;
    private float speedY;
    private int bounceCount = 0;
    private final int maxBounces;

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
    public void move(Projectile projectile) {
        if (projectile.getX() <= 0 && speedX < 0) {
            speedX = -speedX;
            bounceCount++;
        } else if (projectile.getX() >= 1000 && speedX > 0) {
            speedX = -speedX;
            bounceCount++;
        }

        float bottomLimit = App.getFirstCellY();
        float topLimit = App.getFirstCellY() + App.getCellHeight() * 5;

        if (projectile.getY() <= bottomLimit && speedY < 0) {
            speedY = -speedY;
            bounceCount++;
        } else if (projectile.getY() >= topLimit && speedY > 0) {
            speedY = -speedY;
            bounceCount++;
        }

        projectile.setX(projectile.getX() + (speedX * 0.1f));
        projectile.setY(projectile.getY() + (speedY * 0.1f));
    }

    @Override
    public boolean isDead(Projectile projectile) {
        return bounceCount > maxBounces;
    }
}
