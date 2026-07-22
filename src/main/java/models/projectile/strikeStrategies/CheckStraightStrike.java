package models.projectile.strikeStrategies;

import models.Damageable;
import models.core.App;
import models.zombie.Zombie;

public class CheckStraightStrike implements CheckStrike{
    @Override
    public Damageable strike(double x, double y, double oldX, double oldY) {
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()){
            boolean xBetween = (zombie.getX() <= oldX && zombie.getX() >= x) || (zombie.getX() >= oldX && zombie.getX() <= x);
            boolean yBetween = (zombie.getY() <= oldY && zombie.getY() >= y) || (zombie.getY() >= oldY && zombie.getY() <= y);
            if (xBetween && yBetween){
                return zombie;
            }
        }
        return null;
    }

    @Override
    public Damageable strike(double x, double y, Damageable zombie) {
        return null;
    }
}
