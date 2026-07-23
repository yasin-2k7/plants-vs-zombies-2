package models.projectile.strikeStrategies;

import models.Damageable;

public interface CheckStrike {
    Damageable strike(double x, double y, double oldX, double oldY);
    Damageable strike(double x, double y, Damageable damageable);
}
