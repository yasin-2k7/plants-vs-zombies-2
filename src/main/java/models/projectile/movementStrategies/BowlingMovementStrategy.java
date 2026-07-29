package models.projectile.movementStrategies;

import models.Damageable;
import models.core.App;
import models.projectile.Projectile;

import java.util.Random;

public class BowlingMovementStrategy implements MovementStrategy {
    float speedX;
    float speedY;
    Random random = new Random();

    public BowlingMovementStrategy(float speedX, float speedY) {
        this.speedX = speedX*12;
        this.speedY = speedY*12;
    }

    @Override
    public float changeOriginY() {
        return 0;
    }

    @Override
    public void move(Projectile projectile) {
        if (projectile.getY() >= (App.getFirstCellY() + App.getCellHeight() * 5) && speedY > 0) {
            speedY *= -1;
        } else if (projectile.getY() <= App.getFirstCellY() && speedY < 0) {
            speedY *= -1;
        }
        projectile.setX(projectile.getX() + (speedX));
        projectile.setY(projectile.getY() + (speedY));
    }

    public void onHit(Damageable target, Projectile projectile) {
        if (target != null && !projectile.getLastTarget().contains(target)) {
            int sign;
            if (speedY == 0) {
                if (target.getY() == App.getFirstCellY() + App.getCellHeight() / 2.0f) {
                    sign = 1;
                } else if (target.getY() == App.getFirstCellY() + 9 * App.getCellHeight() / 2.0f) {
                    sign = -1;
                } else {
                    sign = random.nextInt(2) * 2 - 1;
                }
                speedY = (float) (speedX * Math.sqrt(2) / 2 * sign);
                speedX = (float) (speedX * Math.sqrt(2) / 2);
            } else {
                speedY *= -1;
            }
        }
    }

    @Override
    public boolean isDead(Projectile projectile) {
        return projectile.getX() > 1000 || projectile.getX() < 0 || projectile.getY() > 1000 || projectile.getY() < 0;
    }
}
