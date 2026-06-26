package models.plant.visions;

import models.core.App;
import models.plant.Plant;
import models.projectile.Projectile;
import models.world.GameWorld;
import models.zombie.Zombie;

public class RotatedVisionStrategy implements VisionStrategy {
    private final float angle;
    private final float width;
    private final float range;

    public RotatedVisionStrategy(float angle, float width, float range) {
        this.angle = angle;
        this.width = width;
        this.range = range;
    }



    @Override
    public Zombie findZombie(Plant owner) {
        GameWorld gameWorld = App.getCurrentGame();

        for (Zombie zombie : gameWorld.getActiveZombies()){
            float xRel = zombie.getX() - owner.getX();
            float yRel = zombie.getY() - owner.getY();
            double xPrime = xRel * Math.cos(angle) + yRel * Math.sin(angle);
            double yPrime = -xRel * Math.sin(angle) + yRel * Math.cos(angle);

            if ((xPrime > 0 && xPrime < range) && (yPrime > -width/2 && yPrime < width/2)){
                return zombie;
            }
        }
        return null;
    }

    @Override
    public boolean isOutOfRange(Projectile projectile) {
        float xRel = projectile.getX() - projectile.getOriginX();
        float yRel = projectile.getY() - projectile.getOriginY();
        double xPrime = xRel * Math.cos(angle) + yRel * Math.sin(angle);
        return xPrime > range;
    }
}