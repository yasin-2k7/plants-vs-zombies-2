package models.projectile.strikeStrategies;

import models.zombie.Zombie;

public class CheckLobbedStrike implements CheckStrike{
    @Override
    public Zombie strike(double x, double y, double oldX, double oldY) {
        return null;
    }

    @Override
    public Zombie strike(double x, double y, Zombie zombie) {
        if (Math.abs(zombie.getX() - x) < 51 && Math.abs(zombie.getY() - y) < 51){
            return zombie;
        }
        return null;
    }
}
