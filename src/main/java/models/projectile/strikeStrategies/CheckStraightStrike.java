package models.projectile.strikeStrategies;

import models.Damageable;
import models.core.App;
import models.world.obstacles.Obstacle;
import models.zombie.Zombie;

public class CheckStraightStrike implements CheckStrike {
    @Override
    public Damageable strike(double x, double y, double oldX, double oldY) {
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()) {
            if (isCollidingWithCircle(oldX, oldY, x, y, zombie.getX(), zombie.getY(), 40)) {
                return zombie;
            }
        }
        for (Obstacle obstacle : App.getCurrentGame().getActiveObstacles()) {
            if (isCollidingWithCircle(oldX, oldY, x, y, obstacle.getX(), obstacle.getY(), 40)) {
                return obstacle;
            }
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

    private boolean isCollidingWithCircle(double x1, double y1, double x2, double y2,
                                          double cx, double cy, double radius) {
        double l2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

        if (l2 == 0) {
            return Math.hypot(cx - x1, cy - y1) <= radius;
        }

        double t = ((cx - x1) * (x2 - x1) + (cy - y1) * (y2 - y1)) / l2;
        t = Math.max(0, Math.min(1, t));

        double closestX = x1 + t * (x2 - x1);
        double closestY = y1 + t * (y2 - y1);

        double distanceSquared = (cx - closestX) * (cx - closestX) + (cy - closestY) * (cy - closestY);
        return distanceSquared <= (radius * radius);
    }



    @Override
    public Damageable strike(double x, double y, Damageable zombie) {
        return null;
    }
}
