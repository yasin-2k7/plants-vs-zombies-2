package models.plant.visions;

import models.core.App;
import models.plant.Plant;
import models.projectile.Projectile;
import models.world.GameWorld;
import models.zombie.Zombie;

public class StraightVisionStrategy implements VisionStrategy {
    private final float range;
    private final float width;
    private final boolean needZombie;

    public StraightVisionStrategy(float range, float width, boolean needZombie) {
        this.range = range;
        this.width = width;
        this.needZombie = needZombie;
    }

    @Override
    public Zombie findZombie(Plant owner) {
        GameWorld gameWorld = App.getCurrentGame();
        if (!needZombie){
            for (Zombie zombie : gameWorld.getActiveZombies()){
                if (VisionStrategy.isBetween(zombie.getX(), owner.getX(), owner.getX() + range) && VisionStrategy.isBetween(zombie.getY(), owner.getY() - width/2, owner.getY() + width/2)){
                    return zombie;
                }
            }
            return null;
        }
        else{
            float x = 1000f;
            Zombie firstZombie = null;
            for (Zombie zombie : gameWorld.getActiveZombies()){
                if (VisionStrategy.isBetween(zombie.getX(), owner.getX(), owner.getX() + range) && VisionStrategy.isBetween(zombie.getY(), owner.getY() - width/2, owner.getY() + width/2)){
                    if (zombie.getX() < x){
                        x = zombie.getX();
                        firstZombie = zombie;
                    }
                }
            }
            return firstZombie;
        }
    }

    @Override
    public boolean isOutOfRange(Projectile projectile) {
        return (!VisionStrategy.isBetween(projectile.getX(), projectile.getOriginX(), projectile.getOriginX() + range) || !VisionStrategy.isBetween(projectile.getY(), projectile.getOriginY() - width/2, projectile.getOriginY() + width/2));
    }
}
