package models.projectile;

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
        if (type.movement.equals("STRAIGHT")){
            Zombie zombie = strikeStrategy.strike(x, y);
            if (zombie != null){
                hitStrategy.applyDamage(zombie, App.getCurrentGame().getActiveZombies(), this);
                pierce--;
                if (pierce == 0){
                    App.getCurrentGame().getActiveProjectiles().remove(this);
                    App.getCurrentGame().getProjectilesPool().release(this);
                }
            }
        }
        else if (type.movement.equals("LOBBED")){
            Zombie zombie = strikeStrategy.strike(x, y, target);
            if (zombie != null){
                hitStrategy.applyDamage(zombie, App.getCurrentGame().getActiveZombies(), this);
                pierce--;
                if (pierce == 0){
                    App.getCurrentGame().getActiveProjectiles().remove(this);
                    App.getCurrentGame().getProjectilesPool().release(this);
                }
            }
        }

        movementStrategy.move(this);
        // deleting out of screen projectiles...
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

    @Override
    public void reset(float x, float y, float targetX, float targetY, HitStrategy hitStrategy, MovementStrategy movementStrategy, CheckStrike checkStrike, ProjectileType type) {
        this.x = x;
        this.y = y;
        originX = x;
        originY = y;
        this.hitStrategy = hitStrategy;
        this.movementStrategy = movementStrategy;
        this.strikeStrategy = checkStrike;
        this.type = type;
        pierce = 1;
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

    public double distanceToZombie(Zombie zombie){
        return (Math.sqrt((zombie.getX() - x)*(zombie.getX() - x) + (zombie.getY() - y)*(zombie.getY() - y)));
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
}