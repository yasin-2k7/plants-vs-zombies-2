package models.projectile;

import models.Damageable;
import models.core.App;
import models.enums.ProjectileType;
import models.plant.components.SunProducerComponent;
import models.plant.visions.VisionStrategy;
import models.pool.Resettable;
import models.projectile.hitStrategies.HitStrategy;
import models.projectile.movementStrategies.MovementStrategy;
import models.projectile.strikeStrategies.CheckStrike;
import models.zombie.Zombie;

public class Projectile implements Resettable{
    private float x, y;
    private float originX, originY;
    private float targetX, targetY;
    private HitStrategy hitStrategy;
    private MovementStrategy movementStrategy;
    private CheckStrike strikeStrategy;
    private VisionStrategy visionStrategy;
    private ProjectileType type;
    private Zombie target;
    private int pierce;

    public void update() {
        Zombie zombie = null;
        if (type.movement.equals("STRAIGHT")) {
            zombie = strikeStrategy.strike(x, y);
        } else if (type.movement.equals("LOBBED")) {
            zombie = strikeStrategy.strike(x, y, target);
        }
        if (zombie != null) {
            hitStrategy.applyDamage(zombie, App.getCurrentGame().getActiveTargets(), this);
            pierce--;
            if (pierce == 0) {
                App.getCurrentGame().getActiveProjectiles().remove(this);
                App.getCurrentGame().getProjectilesPool().release(this);
            }
        }

        if (movementStrategy.isDead(this)){
            App.getCurrentGame().getActiveProjectiles().remove(this);
            App.getCurrentGame().getProjectilesPool().release(this);
        }

        movementStrategy.move(this);
    }

    @Override
    public void reset(float x, float y, int size, SunProducerComponent component) {}
    @Override
    public void reset(float x, float y) {}

    @Override
    public void reset(float x, float y, HitStrategy hitStrategy, MovementStrategy movementStrategy, CheckStrike checkStrike, ProjectileType type) {
        this.x = x;
        this.y = y;
        originX = x;
        originY = y;
        this.hitStrategy = hitStrategy;
        this.movementStrategy = movementStrategy;
        this.strikeStrategy = checkStrike;
        this.type = type;
        pierce = 1;
        targetX = 0;
        targetY = 0;
    }

    public void setPierce(int pierce) {
        this.pierce = pierce;
    }

    public void setHitStrategy(HitStrategy hitStrategy) {
        this.hitStrategy = hitStrategy;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public ProjectileType getType() {
        return type;
    }

    public void setType(ProjectileType type) {
        this.type = type;
    }

    public double distanceTo(Damageable target){
        return (Math.sqrt((target.getX() - x)*(target.getX() - x) + (target.getY() - y)*(target.getY() - y)));
    }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    public CheckStrike getStrikeStrategy() {
        return strikeStrategy;
    }

    public void setTarget(Zombie target) {
        this.target = target;
        targetX = target.getX();
        targetY = target.getY();
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }
}