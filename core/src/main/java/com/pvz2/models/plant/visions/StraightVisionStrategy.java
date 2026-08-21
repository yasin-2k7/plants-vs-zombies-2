package com.pvz2.models.plant.visions;

import com.pvz2.models.Damageable;
import com.pvz2.models.core.App;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.ChapterWorld.FrostbiteCavesWorld;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.obstacles.Obstacle;
import com.pvz2.models.zombie.Zombie;

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
    public Damageable findZombie(Plant owner) {
        GameWorld gameWorld = App.getCurrentGame();
        if (!needZombie) {
            for (Zombie zombie : gameWorld.getActiveZombies()) {
                if (VisionStrategy.isBetween(zombie.getX(), owner.getX(),
                    owner.getX() + range) &&
                        VisionStrategy.isBetween(zombie.getY(), owner.getY() - width / 2,
                            owner.getY() + width / 2)) {
                    return zombie;
                }
            }
            for (Obstacle obstacle : gameWorld.getActiveObstacles()) {
                if (VisionStrategy.isBetween(obstacle.getX(), owner.getX(), owner.getX() + range) &&
                        VisionStrategy.isBetween(obstacle.getY(),owner.getY() - width / 2,
                            owner.getY() + width / 2)) {
                    return obstacle;
                }
            }
            if (gameWorld instanceof FrostbiteCavesWorld){
                for (Plant plant : gameWorld.getActivePlants()) {
                    if (!plant.isFreeze()) continue;
                    if (VisionStrategy.isBetween(plant.getX(), owner.getX(), owner.getX() + range) &&
                        VisionStrategy.isBetween(plant.getY(),owner.getY() - width / 2,
                            owner.getY() + width / 2)) {
                        return plant;
                    }
                }
            }
            return null;
        } else {
            float x = 3000f;
            Zombie firstZombie = null;
            Obstacle firstObstacle = null;
            Plant firstPlant = null;
            for (Zombie zombie : gameWorld.getActiveZombies()) {
                if (VisionStrategy.isBetween(zombie.getX(), owner.getX(), owner.getX() + range) &&
                        VisionStrategy.isBetween(zombie.getY(), owner.getY() - width / 2,
                            owner.getY() + width / 2)) {
                    if (zombie.getX() < x) {
                        x = zombie.getX();
                        firstZombie = zombie;
                    }
                }
            }
            if (firstZombie != null) return firstZombie;
            if (gameWorld instanceof FrostbiteCavesWorld){
                for (Plant plant : gameWorld.getActivePlants()) {
                    if (!plant.isFreeze()) continue;
                    if (VisionStrategy.isBetween(plant.getX(), owner.getX(), owner.getX() + range) &&
                        VisionStrategy.isBetween(plant.getY(), owner.getY() - width / 2,
                            owner.getY() + width / 2)) {
                        if (plant.getX() < x) {
                            x = plant.getX();
                            firstPlant = plant;
                        }
                    }
                }
            }
            if (firstPlant != null) return firstPlant;
            for (Obstacle obstacle : gameWorld.getActiveObstacles()) {
                if (VisionStrategy.isBetween(obstacle.getX(), owner.getX(), owner.getX() + range) &&
                        VisionStrategy.isBetween(obstacle.getY(),owner.getY() - width / 2,
                            owner.getY() + width / 2)) {
                    if (obstacle.getX() < x) {
                        x = obstacle.getX();
                        firstObstacle = obstacle;
                    }
                }
            }
            return firstObstacle;
        }
    }

}
