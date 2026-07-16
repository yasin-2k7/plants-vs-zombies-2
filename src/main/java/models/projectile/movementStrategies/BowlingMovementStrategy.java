package models.projectile.movementStrategies;

import models.core.App;
import models.projectile.Projectile;
import models.zombie.Zombie;

import java.util.Random;

public class BowlingMovementStrategy implements MovementStrategy{
    float speedX;
    float speedY;
    Random random = new Random();

    public BowlingMovementStrategy(float speedX, float speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public float changeOriginY() {
        return 0;
    }

    @Override
    public void move(Projectile projectile) {
        Zombie zombie = projectile.getStrikeStrategy().strike(projectile.getX(), projectile.getY(), projectile.getX()-speedX*5, projectile.getY()-speedY*5);
        int sign;
        if (zombie != null){
            if (speedY == 0){
                if (zombie.getY() == App.getFirstCellY() + App.getCellHeight() / 2){
                    sign = 1;
                }
                else if (zombie.getY() == App.getFirstCellY() + 9 * App.getCellHeight() / 2){
                    sign = -1;
                }
                else {
                    sign = random.nextInt(2) * 2 - 1;
                }
                speedY = (float) (speedX * Math.sqrt(2) / 2 * sign);
                speedX = (float) (speedX * Math.sqrt(2) / 2);
            }
            else {
                speedY *= -1;
            }
        }
        else if (projectile.getY() >= (App.getFirstCellY() + App.getCellHeight() * 5) && speedY > 0) {
            speedY *= -1;
        } else if (projectile.getY() <= App.getFirstCellY() && speedY < 0) {
            speedY *= -1;
        }
        projectile.setX(projectile.getX() + (speedX * 5));
        projectile.setY(projectile.getY() + (speedY * 5));
    }

    @Override
    public boolean isDead(Projectile projectile) {
        return projectile.getX() > 1000 || projectile.getX() < 0 || projectile.getY() > 1000 || projectile.getY() < 0;
    }
}
