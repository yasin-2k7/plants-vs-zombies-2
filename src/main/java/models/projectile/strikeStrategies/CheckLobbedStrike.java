package models.projectile.strikeStrategies;

import models.Damageable;

import java.util.List;

public class CheckLobbedStrike implements CheckStrike {
    @Override
    public Damageable strike(double x, double y, double oldX, double oldY, List<Damageable> lastTargets) {
        return null;
    }

    @Override
    public Damageable strike(double x, double y, Damageable zombie) {
        if (Math.abs(zombie.getX() - x) < 51 && Math.abs(zombie.getY() - y) < 51) {
            return zombie;
        }
        return null;
    }
}
