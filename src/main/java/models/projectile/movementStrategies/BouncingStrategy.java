package models.projectile.movementStrategies;

import models.core.App;
import models.projectile.Projectile;

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
    public void move(Projectile projectile) {
        if (projectile.getX() <= 0 && speedX < 0) {
            speedX = -speedX;
            bounceCount++;
            projectile.getLastTarget().clear();
        } else if (projectile.getX() >= 1000 && speedX > 0) {
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

        projectile.setX(projectile.getX() + (speedX * 12));
        projectile.setY(projectile.getY() + (speedY * 12));
    }



    @Override
    public boolean isDead(Projectile projectile) {
        return bounceCount > maxBounces;
    }
}
