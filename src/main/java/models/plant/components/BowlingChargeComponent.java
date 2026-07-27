package models.plant.components;

import models.core.App;
import models.enums.ProjectileType;
import models.plant.GameComponent;
import models.plant.Plant;
import models.plant.visions.StraightVisionStrategy;
import models.plant.visions.VisionStrategy;
import models.projectile.Projectile;
import models.projectile.hitStrategies.CombinedDamageStrategy;
import models.projectile.movementStrategies.MovementStrategy;
import models.projectile.strikeStrategies.CheckStraightStrike;
import models.projectile.strikeStrategies.CheckStrike;

import java.util.function.Supplier;

public class BowlingChargeComponent implements GameComponent {

    private final Bulb[] bulbs = new Bulb[3];
    private final int shootingTime;
    private final VisionStrategy visionStrategy = new StraightVisionStrategy(1000, App.getCellHeight(), false);
    private final CombinedDamageStrategy[] damageStrategies = new CombinedDamageStrategy[3];
    private final CheckStrike strikeStrategy = new CheckStraightStrike();
    private final Supplier<MovementStrategy> movementStrategy;
    private final CombinedDamageStrategy plantFoodDamageStrategy;
    private int shootingTimer = 0;
    private boolean activePlantFood;
    private int plantFoodProjectileCount = 0;
    public BowlingChargeComponent(int shootingTime,
                                  CombinedDamageStrategy firstDamageStrategy,
                                  CombinedDamageStrategy secondDamageStrategy,
                                  CombinedDamageStrategy thirdDamageStrategy,
                                  Supplier<MovementStrategy> movementStrategy,
                                  CombinedDamageStrategy plantFoodDamageStrategy,
                                  int firstCharge, int secondCharge, int thirdCharge) {
        this.shootingTime = shootingTime;
        damageStrategies[0] = firstDamageStrategy;
        damageStrategies[1] = secondDamageStrategy;
        damageStrategies[2] = thirdDamageStrategy;
        this.movementStrategy = movementStrategy;
        this.plantFoodDamageStrategy = plantFoodDamageStrategy;
        bulbs[0] = new Bulb(ProjectileType.LARGE_BULB, firstCharge);
        bulbs[1] = new Bulb(ProjectileType.MEDIUM_BULB, secondCharge);
        bulbs[2] = new Bulb(ProjectileType.SMALL_BULB, thirdCharge);
    }

    @Override
    public void activatePlantFood(Plant owner) {
        activePlantFood = true;
        shootingTimer = 0;
        plantFoodProjectileCount = 3;
        for (Bulb bulb : bulbs) {
            bulb.isReady = true;
        }
    }

    @Override
    public void update(Plant owner) {
        if (activePlantFood) {
            plantFoodHandler(owner);
            return;
        }

        for (Bulb bulb : bulbs) {
            bulb.update();
        }

        if (visionStrategy.findZombie(owner) != null) {
            if (shootingTimer > 0) {
                shootingTimer--;
            } else {
                tryShooting(owner);
            }
        }
    }

    private void tryShooting(Plant owner) {
        for (int i = 0; i < 3; i++) {
            if (bulbs[i].isReady) {
                bulbs[i].isReady = false;
                shootingTimer = shootingTime;
                Projectile p = App.getCurrentGame().getProjectilesPool().acquire();
                p.reset(owner.getX(), owner.getY(), damageStrategies[i],
                        movementStrategy.get(), strikeStrategy, bulbs[i].projectileType);
                p.setPierce(1000);
                App.getCurrentGame().getActiveProjectiles().add(p);
                break;
            }
        }
    }

    private void plantFoodHandler(Plant owner) {
        if (shootingTimer > 0) {
            shootingTimer--;
        } else {
            plantFoodProjectileCount--;
            shootingTimer = shootingTime;
            Projectile p = App.getCurrentGame().getProjectilesPool().acquire();
            p.reset(owner.getX(), owner.getY(), plantFoodDamageStrategy,
                    movementStrategy.get(), strikeStrategy, ProjectileType.SPECIAL_BULB);
            App.getCurrentGame().getActiveProjectiles().add(p);
            if (plantFoodProjectileCount == 0) {
                activePlantFood = false;
            }
        }
    }

    private static class Bulb {
        private final ProjectileType projectileType;
        private final int chargeTime;
        private int currentCharge = 0;
        private boolean isReady;


        public Bulb(ProjectileType projectileType, int chargeTime) {
            this.projectileType = projectileType;
            this.chargeTime = chargeTime;
        }

        public void update() {
            if (!isReady) {
                currentCharge++;
                if (currentCharge >= chargeTime) {
                    isReady = true;
                    currentCharge = 0;
                }
            }
        }
    }
}
