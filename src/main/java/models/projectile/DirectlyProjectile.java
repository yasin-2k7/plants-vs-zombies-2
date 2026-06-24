package models.projectile;

import models.core.App;
import models.plant.components.SunProducerComponent;
import models.pool.Resettable;
import models.projectile.strategy.HitStrategy;
import models.zombie.Zombie;

public class DirectlyProjectile implements Resettable , Projectile{
    private ProjectileModel model;
    private double x, y;
    private HitStrategy strategy;

    @Override
    public void update() {
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()){
            if (Math.abs(zombie.getX() - x) < 0.05 && zombie.getY() == y){
                strategy.applyDamage(zombie);
                App.getCurrentGame().getActiveProjectiles().remove(this);

            }
        }
    }

    @Override
    public void reset(float x, float y, int size, SunProducerComponent component) {}
    @Override
    public void reset(float x, float y) {}
    @Override
    public void reset(float x, float y, HitStrategy strategy) {
        this.x = x;
        this.y = y;
        this.strategy = strategy;
    }

    public void setStrategy(HitStrategy strategy) {
        this.strategy = strategy;
    }

    public void setModel(ProjectileModel model) {
        this.model = model;
    }
}