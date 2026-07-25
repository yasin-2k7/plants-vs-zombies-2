package models.plant.components;

import models.Damageable;
import models.core.App;
import models.enums.ProjectileType;
import models.plant.GameComponent;
import models.plant.Plant;
import models.plant.components.shooterPlantFoodBehaviors.BurstPlantFood;
import models.plant.components.shooterPlantFoodBehaviors.PlantFoodBehavior;
import models.plant.visions.VisionStrategy;
import models.projectile.Projectile;
import models.projectile.hitStrategies.CombinedDamageStrategy;
import models.projectile.movementStrategies.MovementStrategy;
import models.projectile.strikeStrategies.CheckStrike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ShooterComponent implements GameComponent {
    private static final int BURST_DELAY_MAX = 3;
    private final int shootingTime;
    public PlantFoodBehavior plantFoodBehavior;
    Damageable target = null;
    private ProjectileType bulletType;
    private ProjectileType giantType;
    private List<Supplier<MovementStrategy>> defaultMovementStrategies = new ArrayList<>();
    private int burstProjectileNumber;
    private int burstProjectileNumberOnPlantFood;
    private int burstDelayMax = BURST_DELAY_MAX;
    private int projectilesLeftForShoot;
    private int shootingTimer = 0;
    private int burstDelayTimer;
    private boolean hasGiant;
    private int giantCount;
    private boolean activePlantFood;
    private int normalPierce;
    private int giantPierce;
    private Supplier<CombinedDamageStrategy> damageStrategy;
    private CheckStrike strikeStrategy;
    private List<VisionStrategy> visions = new ArrayList<>();
    private List<Supplier<MovementStrategy>> movementStrategies = new ArrayList<>();
    private CombinedDamageStrategy plantFoodStrategy;
    private AttackCallback attackCallback;

    public ShooterComponent(ProjectileType bulletType, ProjectileType giantType,
                            int shootingTime, int burstProjectileNumber,
                            int burstProjectileNumberOnPlantFood, boolean hasGiant,
                            Supplier<CombinedDamageStrategy> damageStrategy, CheckStrike strikeStrategy,
                            int giantCount, int normalPierce, int giantPierce, int giantDamageFactor) {
        this.bulletType = bulletType;
        this.giantType = giantType;
        this.shootingTime = shootingTime;
        this.burstProjectileNumber = burstProjectileNumber;
        this.burstProjectileNumberOnPlantFood = burstProjectileNumberOnPlantFood;
        this.hasGiant = hasGiant;
        this.damageStrategy = damageStrategy;
        this.strikeStrategy = strikeStrategy;
        this.giantCount = giantCount;
        this.normalPierce = normalPierce;
        this.giantPierce = giantPierce;
        this.plantFoodBehavior = BurstPlantFood.INSTANCE;
        plantFoodStrategy = damageStrategy.get().changeDamage(damageStrategy.get().getDamage() * giantDamageFactor);
    }

    public void setPlantFoodBehavior(PlantFoodBehavior plantFoodBehavior) {
        this.plantFoodBehavior = plantFoodBehavior;
    }

    @Override
    public void activatePlantFood(Plant owner) {
        if (plantFoodBehavior != null) {
            this.defaultMovementStrategies = new ArrayList<>(this.movementStrategies);
            plantFoodBehavior.activate(owner, this);
        }
    }

    @Override
    public void update(Plant owner) {
        if (projectilesLeftForShoot > 0) {
            burstHandler(owner);
            return;
        }


        for (VisionStrategy visionStrategy : visions) {
            if (visionStrategy.findZombie(owner) != null) {
                if (attackCallback != null) {
                    attackCallback.onAttack(owner);
                }
                target = visionStrategy.findZombie(owner);
                if (shootingTimer > 0) {
                    shootingTimer--;
                } else {
                    this.projectilesLeftForShoot = burstProjectileNumber;
                    burstDelayMax = BURST_DELAY_MAX;
                    burstDelayTimer = 0;
                    shootingTimer = shootingTime;
                }
                break;
            }
        }

    }

    private void burstHandler(Plant owner) {
        if (burstDelayTimer > 0) {
            burstDelayTimer--;
        } else {
            for (Supplier<MovementStrategy> movementStrategy : movementStrategies) {
                Projectile p = App.getCurrentGame().getProjectilesPool().acquire();

                if (activePlantFood && projectilesLeftForShoot <= giantCount && hasGiant) {
                    p.reset(owner.getX(), owner.getY() + movementStrategy.get().changeOriginY(),
                            plantFoodStrategy, movementStrategy.get(), strikeStrategy, giantType);
                    if (giantPierce != 1) {
                        p.setPierce(giantPierce);
                    }
                } else {
                    p.reset(owner.getX(), owner.getY() + movementStrategy.get().changeOriginY(),
                            damageStrategy.get(), movementStrategy.get(), strikeStrategy, bulletType);
                    if (normalPierce != 1) {
                        p.setPierce(normalPierce);
                    }
                }
                p.setPlantType(owner.getType());

                p.setTarget(target);
                App.getCurrentGame().getActiveProjectiles().add(p);
            }

            projectilesLeftForShoot--;
            burstDelayTimer = burstDelayMax;

            if (projectilesLeftForShoot <= 0) {
                projectilesLeftForShoot = 0;
                if (activePlantFood) {
                    activePlantFood = false;
                    if (!defaultMovementStrategies.isEmpty()) {
                        this.movementStrategies = new ArrayList<>(defaultMovementStrategies);
                        this.defaultMovementStrategies.clear();
                    }
                }
            }
        }
    }

    public List<VisionStrategy> getVisions() {
        return visions;
    }

    public List<Supplier<MovementStrategy>> getMovementStrategies() {
        return movementStrategies;
    }

    public void setBurstProjectileNumber(int burstProjectileNumber) {
        this.burstProjectileNumber = burstProjectileNumber;
    }

    public void setGiantCount(int giantCount) {
        this.giantCount = giantCount;
    }

    public void setAttackCallback(AttackCallback callback) {
        this.attackCallback = callback;
    }

    public void setShootingTimer(int shootingTimer) {
        this.shootingTimer = shootingTimer;
    }

    public void setBurstDelayTimer(int burstDelayTimer) {
        this.burstDelayTimer = burstDelayTimer;
    }

    public void setActivePlantFood(boolean activePlantFood) {
        this.activePlantFood = activePlantFood;
    }

    public int getBurstProjectileNumberOnPlantFood() {
        return burstProjectileNumberOnPlantFood;
    }

    public void setBurstProjectileNumberOnPlantFood(int burstProjectileNumberOnPlantFood) {
        this.burstProjectileNumberOnPlantFood = burstProjectileNumberOnPlantFood;
    }

    public ProjectileType getGiantType() {
        return giantType;
    }

    public CombinedDamageStrategy getPlantFoodStrategy() {
        return plantFoodStrategy;
    }

    public void setPlantFoodStrategy(CombinedDamageStrategy plantFoodStrategy) {
        this.plantFoodStrategy = plantFoodStrategy;
    }

    public CheckStrike getStrikeStrategy() {
        return strikeStrategy;
    }

    public void setDamageStrategy(Supplier<CombinedDamageStrategy> damageStrategy) {
        this.damageStrategy = damageStrategy;
    }

    public int getProjectilesLeftForShoot() {
        return projectilesLeftForShoot;
    }

    public void setProjectilesLeftForShoot(int projectilesLeftForShoot) {
        this.projectilesLeftForShoot = projectilesLeftForShoot;
    }

    public ProjectileType getBulletType() {
        return bulletType;
    }

    public void setBulletType(ProjectileType bulletType) {
        this.bulletType = bulletType;
    }

    public interface AttackCallback {
        void onAttack(Plant owner);
    }
}