package models.projectile.strikeStrategies;

import models.Damageable;
import models.core.App;
import models.plant.Plant;

public class CheckPlantStrike implements CheckStrike {
    @Override
    public Damageable strike(double x, double y, double oldX, double oldY) {
        for (Plant plant : App.getCurrentGame().getActivePlants()) {
            boolean xBetween = (plant.getX() <= oldX && plant.getX() >= x) || (plant.getX() >= oldX && plant.getX() <= x);
            boolean yBetween = (plant.getY() <= oldY && plant.getY() >= y) || (plant.getY() >= oldY && plant.getY() <= y);
            if (xBetween && yBetween) {
                return plant;
            }
        }
        return null;
    }

    @Override
    public Damageable strike(double x, double y, Damageable damageable) {
        return null;
    }
}
