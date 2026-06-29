package models.projectile.strikeStrategies;

import models.core.App;
import models.zombie.Zombie;

public class CheckStraightStrike implements CheckStrike{
    @Override
    public Zombie strike(double x, double y) {
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()){
            if (Math.abs(zombie.getX() - x) < 0.05 && Math.abs(zombie.getY() - y) < 0.05){
                return zombie;
            }
        }
        return null;
    }

    @Override
    public Zombie strike(double x, double y, Zombie zombie) {
        return null;
    }
}
