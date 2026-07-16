package models.projectile.strikeStrategies;

import models.zombie.Zombie;

public interface CheckStrike {
    Zombie strike(double x, double y, double oldX, double oldY);
    Zombie strike(double x, double y, Zombie zombie);
}
