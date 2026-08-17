package com.pvz2.models.plant.components;

import com.pvz2.models.Damageable;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.ProjectileType;
import com.pvz2.models.plant.AnimationDurations;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.plant.components.shooterPlantFoodBehaviors.BurstPlantFood;
import com.pvz2.models.plant.components.shooterPlantFoodBehaviors.PlantFoodBehavior;
import com.pvz2.models.plant.visions.VisionStrategy;
import com.pvz2.models.projectile.Projectile;
import com.pvz2.models.projectile.hitStrategies.CombinedDamageStrategy;
import com.pvz2.models.projectile.movementStrategies.MovementStrategy;
import com.pvz2.models.projectile.strikeStrategies.CheckStrike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ShooterComponent implements GameComponent {
    private static final float BURST_DELAY_MAX = 0.1f;
    private static final float DEFAULT_ACTION_TIME = 0.3f;
    private static final float DEFAULT_ACTION_TIME_INTERVAL = 0.5f;
    private static final String ATTACK_CLIP = "attack";

    private final float shootingTime;
    public PlantFoodBehavior plantFoodBehavior;
    Damageable target = null;
    private ProjectileType bulletType;
    private ProjectileType giantType;
    private List<Supplier<MovementStrategy>> defaultMovementStrategies = new ArrayList<>();
    private int burstProjectileNumber;
    private int burstProjectileNumberOnPlantFood;
    private float burstDelayMax = BURST_DELAY_MAX;
    private int projectilesLeftForShoot;
    private float shootingTimer = 0f;
    private float burstDelayTimer;
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

    // resolved lazily from AnimationDurations once the owner's PlantType is known
    private float actionTime = -1f;
    private float currentActionTimer = 0f;

    public ShooterComponent(ProjectileType bulletType, ProjectileType giantType,
                            float shootingTime, int burstProjectileNumber,
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

    private void ensureTimingLoaded(Plant owner) {
        if (actionTime >= 0f) return;
        actionTime = AnimationDurations.getReleaseTime(owner.getType(), ATTACK_CLIP, DEFAULT_ACTION_TIME);
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
    public void update(Plant owner, float delta) {
        ensureTimingLoaded(owner);

        if (projectilesLeftForShoot > 0) {
            burstHandler(owner, delta);
            return;
        }

        for (VisionStrategy visionStrategy : visions) {
            if (visionStrategy.findZombie(owner) != null) {
                target = visionStrategy.findZombie(owner);

                if (shootingTimer > 0) {
                    shootingTimer -= delta;
                    break;
                }

                if (owner.getState() == Plant.State.IDLE) {
                    owner.setState(Plant.State.ATTACK);
                    currentActionTimer = 0f;
                }

                currentActionTimer += delta;
                if (currentActionTimer < actionTime) {
                    break;
                }

                if (attackCallback != null) {
                    attackCallback.onAttack(owner);
                }
                this.projectilesLeftForShoot = burstProjectileNumber;
                burstDelayMax = BURST_DELAY_MAX;
                burstDelayTimer = 0f;
                shootingTimer = shootingTime;
                break;
            }
        }
    }

    private void burstHandler(Plant owner, float delta) {
        if (burstDelayTimer > 0) {
            burstDelayTimer -= delta;
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
                owner.setState(Plant.State.IDLE);
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
