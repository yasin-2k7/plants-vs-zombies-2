package models.projectile.strikeStrategies;

import models.Damageable;
import models.core.App;
import models.world.obstacles.Obstacle;
import models.zombie.Zombie;

public class CheckStraightStrike implements CheckStrike {
    @Override
    public Damageable strike(double x, double y, double oldX, double oldY) {
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()) {
            if (isBetween(x, y, oldX, oldY, zombie.getX(), zombie.getY())) return zombie;
        }
        for (Obstacle obstacle : App.getCurrentGame().getActiveObstacles()) {
            if (isBetween(x, y, oldX, oldY, obstacle.getX(), obstacle.getY()))
                return obstacle;
        }
        return null;
    }

    static boolean isBetween(double x, double y, double oldX, double oldY, float x2, float y2) {
        boolean xBetween =
                (x2 <= oldX && x2 >= x) || (x2 >= oldX && x2 <= x);
        boolean yBetween =
                (y2 <= oldY && y2 >= y) || (y2 >= oldY && y2 <= y);
        if (xBetween && yBetween) {
            return true;
        }
        return false;
    }

    @Override
    public Damageable strike(double x, double y, Damageable zombie) {
        return null;
    }
}
