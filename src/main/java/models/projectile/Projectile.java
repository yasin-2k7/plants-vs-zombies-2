package models.projectile;

import models.Damageable;
import models.core.App;
import models.enums.PlantType;
import models.enums.ProjectileType;
import models.plant.Plant;
import models.plant.components.SunProducerComponent;
import models.pool.Resettable;
import models.projectile.hitStrategies.HitStrategy;
import models.projectile.hitStrategies.PlantDamageStrategy;
import models.projectile.movementStrategies.MovementStrategy;
import models.projectile.strikeStrategies.CheckStrike;
import models.world.Cell;
import models.zombie.zombiesType.DeflectorZombie;

public class Projectile implements Resettable {
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

    void checkProjectilesTowardPlants(double oldX, double oldY) {
        Damageable plantTarget = null;
        if (type != null && type.movement != null) {
            plantTarget = strikeStrategy.strike(x, y, oldX, oldY);
        }

        if (plantTarget instanceof Plant plant) {
            if (hitStrategy != null) {
                hitStrategy.applyDamage(plant, this);
            }

            pierce--;
            if (pierce == 0) {
                dead = true;
            }
        }
    }

    public void update() {
        double oldX = x;
        double oldY = y;
        movementStrategy.move(this);
        if (hitStrategy instanceof PlantDamageStrategy) {
            checkProjectilesTowardPlants(oldX, oldY);
            return;
        }
        Damageable zombie = null;
        if (type != null && type.movement != null) {

            //  برخورد تیر مستقیم گیاهان به گیاهان یخ‌زده
            if (type.movement.equals("STRAIGHT") && !(hitStrategy instanceof PlantDamageStrategy)) {
                models.world.GameWorld game = App.getCurrentGame();
                if (game != null) {
                    Cell currentCell = game.getCellAt(this.x, this.y);
                    if (currentCell != null && currentCell.getPlant() != null) {
                        Plant p = currentCell.getPlant();
                        // بررسی اینکه گیاه یخ زده باشد و گیاه شلیک‌کننده خودش نباشد
                        if (p.isFreeze() && p.getX() > this.originX + 20) {
                            p.takeDamage(hitStrategy.getDamage(), (models.zombie.Zombie) null);
                            pierce--;
                            if (pierce <= 0) {
                                dead = true;
                                return;
                            }
                        }
                    }
                }
            }

            if (type.movement.equals("STRAIGHT")) {
                zombie = strikeStrategy.strike(x, y, oldX, oldY);
            } else if (type.movement.equals("LOBBED")) {
                zombie = strikeStrategy.strike(x, y, target);
            }
        }
        if (zombie != null) {
            if (zombie instanceof DeflectorZombie deflector) {
                if (deflector.tryDeflect(this)) {
                    dead = true;
                    return;
                }
            }
            if (hitStrategy != null) {
                hitStrategy.applyDamage(zombie, App.getCurrentGame().getActiveTargets(), this);
            }
            pierce--;
            if (pierce == 0) {
                dead = true;
                return;
            }
        }
        if (movementStrategy.isDead(this)) {
            dead = true;
        }
    }

    @Override
    public void reset(float x, float y, int size, SunProducerComponent component) {
    }

    @Override
    public void reset(float x, float y) {
    }

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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public ProjectileType getType() {
        return type;
    }

    public void setType(ProjectileType type) {
        this.type = type;
    }

    public double distanceTo(Damageable target) {
        return Math.sqrt((target.getX() - x) * (target.getX() - x) + (target.getY() - y) * (target.getY() - y));
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
        if (target != null) {
            targetX = target.getX();
            targetY = target.getY();
        }
    }

    public boolean isDead() {
        return dead;
    }

    public HitStrategy getHitStrategy() {
        return hitStrategy;
    }

    public void setHitStrategy(HitStrategy hitStrategy) {
        this.hitStrategy = hitStrategy;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public PlantType getPlantType() {
        return plantType;
    }

    public void setPlantType(PlantType plantType) {
        this.plantType = plantType;
    }
}