package models.projectile.strikeStrategies;

import models.Damageable;

import java.util.List;

public interface CheckStrike {
    Damageable strike(double x, double y, double oldX, double oldY, List<Damageable> lastTargets);

    Damageable strike(double x, double y, Damageable damageable);
}
