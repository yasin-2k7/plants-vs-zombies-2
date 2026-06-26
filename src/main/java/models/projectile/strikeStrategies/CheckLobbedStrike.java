package models.projectile.strikeStrategies;

import models.zombie.Zombie;

public class CheckLobbedStrike implements CheckStrike{
    @Override
    public Zombie strike(double x, double y) {
        return null;
    }

    @Override
    public Zombie strike(double x, double y, Zombie zombie) {
        if (Math.abs(zombie.getX() - x) < 0.5 && Math.abs(zombie.getY() - y) < 0.5){
            return zombie;
        }
        return null;
    }
}
