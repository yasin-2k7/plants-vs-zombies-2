package models.projectile.strikeStrategies;

import models.core.App;
import models.zombie.Zombie;

public class CheckStraightStrike implements CheckStrike{
    @Override
    public Zombie strike(double x, double y) {
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()){
            if (Math.abs(zombie.getX() - x) < 0.05 && zombie.getY() == y){
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
