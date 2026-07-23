package models.projectile.hitStrategies;

import models.Damageable;
import models.enums.ProjectileType;
import models.projectile.Projectile;
import models.world.obstacles.IceBlock;
import models.zombie.Zombie;

import java.util.List;

public class CombinedDamageStrategy implements HitStrategy{
    private int damage;
    private int neighborDamage = 0;
    private float radius = 0;
    private String element = "NORMAL";
    private int chillTime = 50;
    private int poisonDamageOnTick = 5;
    private ProjectileType projectileType;

    public int getPoisonDamageOnTick() {
        return poisonDamageOnTick;
    }

    public void setPoisonDamageOnTick(int poisonDamageOnTick) {
        this.poisonDamageOnTick = poisonDamageOnTick;
    }

    public void setChillTime(int chillTime) {
        this.chillTime = chillTime;
    }

    public int getChillTime() {
        return chillTime;
    }

    private CombinedDamageStrategy(int damage, int neighborDamage, float radius,
                                   String element, int chillTime, int poisonDamageOnTick,
                                   ProjectileType projectileType) {
        this.damage = damage;
        this.neighborDamage = neighborDamage;
        this.radius = radius;
        this.element = element;
        this.chillTime = chillTime;
        this.poisonDamageOnTick = poisonDamageOnTick;
        this.projectileType = projectileType;
    }

    public CombinedDamageStrategy(int damage, ProjectileType projectileType) {
        this.damage = damage;
        this.projectileType = projectileType;
    }


    @Override
    public int getDamage() {
        return damage;
    }

    public CombinedDamageStrategy(int damage, int neighborDamage, float radius, ProjectileType projectileType) {
        this.damage = damage;
        this.neighborDamage = neighborDamage;
        this.radius = radius;
        this.projectileType = projectileType;
    }

    public CombinedDamageStrategy changeDamage(int damage){
        return new CombinedDamageStrategy(damage, neighborDamage, radius,this.element, this.chillTime, this.poisonDamageOnTick, projectileType);
    }

    @Override
    public void applyDamage(Damageable target, List<Damageable> allTargets, Projectile projectile) {
        String type = element != null ? element : "NORMAL";
        target.takeDamage(damage, type);
        applySpecialDamage(target, projectile);

        if (target instanceof Zombie && projectile.getPlantType() != null) {
            ((Zombie) target).setKiller(projectile.getPlantType());
        }

        if (radius > 0) {
            for (Damageable extraTarget : allTargets) {
                if (extraTarget != target && projectile.distanceTo(extraTarget) <= radius) {
                    extraTarget.takeDamage(neighborDamage, type);
                    applySpecialDamage(extraTarget, projectile);
                    if (extraTarget instanceof Zombie && projectile.getPlantType() != null) {
                        ((Zombie) extraTarget).setKiller(projectile.getPlantType());
                    }
                }
            }
        }
    }

    private void applySpecialDamage(Damageable target, Projectile projectile) {
        switch (element) {
            case "POISON":
                if (target instanceof Zombie zombie) {
                    if (projectileType == ProjectileType.GOO){
                        zombie.makePoisoned(poisonDamageOnTick);
                    }
                    else{
                        zombie.makePoisoned(poisonDamageOnTick*100);
                    }

                }
                break;
            case "MOVE":
                if (target instanceof Zombie zombie) {
                    zombie.setX(zombie.getX() + 100);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void increaseDamage(int factor){
        damage *= factor;
    }

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public String getElement() {
        return element;
    }


}
