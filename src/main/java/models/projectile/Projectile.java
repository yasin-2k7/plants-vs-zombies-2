package models.projectile;

import controller.GameMenuController;
import models.Damageable;
import models.core.App;
import models.enums.PlantType;
import models.enums.ProjectileType;
import models.plant.Plant;
import models.plant.components.SunProducerComponent;
import models.plant.visions.VisionStrategy;
import models.pool.Resettable;
import models.projectile.hitStrategies.HitStrategy;
import models.projectile.movementStrategies.MovementStrategy;
import models.projectile.strikeStrategies.CheckStrike;
import models.world.Cell;
import models.zombie.Zombie;
import models.zombie.zombiesType.DeflectorZombie;

public class Projectile implements Resettable{
    private float x, y;
    private float originX, originY;
    private float targetX, targetY;
    private HitStrategy hitStrategy;
    private MovementStrategy movementStrategy;
    private CheckStrike strikeStrategy;
    private ProjectileType type;
    private Damageable target;
    private int pierce;
    private boolean dead = false;
    private PlantType plantType;

    public void update() {
        double oldX = x;
        double oldY = y;
        movementStrategy.move(this);


        if (type.movement.equals("STRAIGHT")) {
            Cell currentCell = App.getCurrentGame().getCellAt(x, y);

            if (currentCell != null && currentCell.getPlant() != null) {
                Plant plant = currentCell.getPlant();
                System.out.println("💥 Zombie Pea hit " + plant.getClass().getSimpleName() + " for "
                        + hitStrategy.getDamage() + " damage!");

                if (plant.isFreeze()) {
                    if (hitStrategy.getElement().equalsIgnoreCase("FIRE")) {
                        plant.unfreeze();
                    } else {
                        plant.takeDamage(hitStrategy.getDamage());
                    }
                } else {
                    plant.takeDamage(hitStrategy.getDamage());
                }
                pierce--;
                if (pierce == 0) {
                    dead = true;
                    App.getCurrentGame().getProjectilesPool().release(this);
                    return;
                }
            }
        }

        Damageable zombie = null;
        if (type.movement.equals("STRAIGHT")) {
            zombie = strikeStrategy.strike(x, y, oldX, oldY);
        } else if (type.movement.equals("LOBBED")) {
            zombie = strikeStrategy.strike(x, y, target);
        }

        if (zombie != null) {
            // بررسی دافع‌ها
            if (zombie instanceof DeflectorZombie deflector) {
                // چتردار: پرتابه‌های lobber را دفع می‌کند
                if (!deflector.isJuggler() && type.movement.equals("LOBBED")) {
                    dead = true;
                    App.getCurrentGame().getProjectilesPool().release(this);
                    GameMenuController.updateState("Parasol deflected a lobbed projectile!");
                    return;
                }
                // ژانگولر: پرتابه‌های مستقیم را بازتاب می‌دهد
                if (deflector.isJuggler() && deflector.tryDeflect(this)) {
                    dead = true;
                    App.getCurrentGame().getProjectilesPool().release(this);
                    return;
                }
            }

            // در غیر این صورت، آسیب عادی اعمال می‌شود
            hitStrategy.applyDamage(zombie, App.getCurrentGame().getActiveTargets(), this);
            pierce--;
            if (pierce == 0) {
                dead = true;
                App.getCurrentGame().getProjectilesPool().release(this);
                return;
            }
        }
    }

    @Override
    public void reset(float x, float y, int size, SunProducerComponent component) {
    }
    @Override
    public void reset(float x, float y) {}

    @Override
    public void reset(float x, float y,
                      HitStrategy hitStrategy,
                      MovementStrategy movementStrategy,
                      CheckStrike checkStrike,
                      ProjectileType type) {
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
        dead = false;
        this.plantType = null;
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

    public void setTarget(Damageable target) {
        this.target = target;
        targetX = target.getX();
        targetY = target.getY();
    }

    public boolean isDead() {
        return dead;
    }

    public HitStrategy getHitStrategy() {
        return hitStrategy;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setPlantType(PlantType plantType) {this.plantType = plantType;}
    public PlantType getPlantType() {return plantType;}
}