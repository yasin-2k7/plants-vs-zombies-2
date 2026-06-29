package models.plant.components;

import models.core.App;
import models.enums.ProjectileType;
import models.plant.GameComponent;
import models.plant.Plant;
import models.plant.visions.VisionStrategy;
import models.projectile.Projectile;
import models.projectile.hitStrategies.CombinedDamageStrategy;
import models.projectile.movementStrategies.MovementStrategy;
import models.projectile.strikeStrategies.CheckStrike;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ShooterComponent implements GameComponent {
    private ProjectileType bulletType;
    private ProjectileType giantType;

    private final int shootingTime;
    private int burstProjectileNumber;
    private int burstProjectileNumberOnPlantFood;
    private final int BURST_DELAY_MAX = 3;

    private int burstDelayMax = BURST_DELAY_MAX;
    private int projectilesLeftForShoot;
    private int shootingTimer = 0;
    private int burstDelayTimer;

    private int giantDamageFactor;
    private boolean hasGiant;
    private int giantCount;
    private boolean activePlantFood;

    private int normalPierce;
    private int giantPierce;

    Zombie target = null;

    private CombinedDamageStrategy damageStrategy;
    private CheckStrike strikeStrategy;
    private List<VisionStrategy> visions = new ArrayList<>();
    private List<Supplier<MovementStrategy>> movementStrategies = new ArrayList<>();

    private CombinedDamageStrategy plantFoodStrategy = damageStrategy.changeDamage(damageStrategy.getDamage()*giantDamageFactor);

    public void setPlantFoodStrategy(CombinedDamageStrategy plantFoodStrategy) {
        this.plantFoodStrategy = plantFoodStrategy;
    }

    public interface AttackCallback {
        void onAttack(Plant owner);
    }

    private AttackCallback attackCallback;

    public ShooterComponent(ProjectileType bulletType, ProjectileType giantType, int shootingTime, int burstProjectileNumber, int burstProjectileNumberOnPlantFood, boolean hasGiant, CombinedDamageStrategy damageStrategy, CheckStrike strikeStrategy, int giantCount, int normalPierce, int giantPierce, int giantDamageFactor) {
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
        this.giantDamageFactor = giantDamageFactor;
    }

    public void activatePlantFood(){
        activePlantFood = true;
        projectilesLeftForShoot = burstProjectileNumberOnPlantFood;
        shootingTimer = 0;
        burstDelayTimer = 0;
    }


    @Override
    public void update(Plant owner) {
        if (projectilesLeftForShoot > 0){
            burstHandler(owner);
            return;
        }


        for (VisionStrategy visionStrategy : visions){
            if (visionStrategy.findZombie(owner) != null){
                if (attackCallback != null) {
                    attackCallback.onAttack(owner);
                }
                target = visionStrategy.findZombie(owner);
                if (shootingTimer > 0){
                    shootingTimer--;
                }
                else {
                    this.projectilesLeftForShoot = burstProjectileNumber;
                    burstDelayMax = BURST_DELAY_MAX;
                    burstDelayTimer = 0;
                    shootingTimer = shootingTime;
                }
                break;
            }
        }

    }

    private void burstHandler(Plant owner){
        if (burstDelayTimer > 0){
            burstDelayTimer--;
        }
        else{
            int i = 0;
            for (Supplier<MovementStrategy> movementStrategy : movementStrategies){
                Projectile p = App.getCurrentGame().getProjectilesPool().acquire();

                if (activePlantFood && projectilesLeftForShoot <= giantCount && hasGiant){
                    p.reset(owner.getX(), owner.getY() + movementStrategy.get().changeOriginY(), plantFoodStrategy, movementStrategy.get(), strikeStrategy, giantType);
                    if (giantPierce != 1) {p.setPierce(giantPierce);}
                }
                else{
                    p.reset(owner.getX(), owner.getY() + movementStrategy.get().changeOriginY(), damageStrategy, movementStrategy.get(),strikeStrategy, bulletType);
                    if (normalPierce != 1) {p.setPierce(normalPierce);}

                }

                App.getCurrentGame().getActiveProjectiles().add(p);
                projectilesLeftForShoot--;
                burstDelayTimer = burstDelayMax;

                if (projectilesLeftForShoot == 0){
                    activePlantFood = false;
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

    public void setBurstProjectileNumberOnPlantFood(int burstProjectileNumberOnPlantFood) {
        this.burstProjectileNumberOnPlantFood = burstProjectileNumberOnPlantFood;
    }

    public void setGiantCount(int giantCount) {
        this.giantCount = giantCount;
    }

    public void setAttackCallback(AttackCallback callback) {
        this.attackCallback = callback;
    }
}