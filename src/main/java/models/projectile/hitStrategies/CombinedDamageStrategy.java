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

    public void setElement(String element){
        this.element = element;
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
        target.takeDamage(damage, "NORMAL");
        applySpecialDamage(target, projectile);
        if (target instanceof Zombie && projectile.getPlantType() != null) {
            ((Zombie) target).setKiller(projectile.getPlantType());
        }
        if (radius > 0) {
            for (Damageable extraTarget : allTargets){
                if (extraTarget != target){
                    if (projectile.distanceTo(extraTarget) <= radius){
                        extraTarget.takeDamage(neighborDamage, "NORMAL");
                        applySpecialDamage(extraTarget, projectile);
                        if (extraTarget instanceof Zombie && projectile.getPlantType() != null) {
                            ((Zombie) extraTarget).setKiller(projectile.getPlantType());
                        }
                    }
                }
            }
        }
    }

    private void applySpecialDamage(Damageable target, Projectile projectile){
        switch (element){
            case "NORMAL":
                break;
            case "ICE":
                if (target instanceof Zombie zombie){
                    zombie.applySlow(chillTime, 0.5, false);
                }
                break;
            case "FIRE":
                if (target instanceof Zombie zombie){
                    if (zombie.getIceHealth() > 0){
                        zombie.setIceHealth(0);
                    }
                    zombie.unfreeze();
                }
                else if (target instanceof IceBlock){
                    target.takeDamage(600, "FIRE");
                }
                break;
            case "STUN":
                if (target instanceof Zombie zombie) {
                    zombie.disableFor(20);
                }
                break;
            case "POISON":
                if (target instanceof Zombie zombie){
                    zombie.setHealth(zombie.getHealth()-poisonDamageOnTick);
                }
                break;
            case "MOVE":
                if (target instanceof Zombie zombie){
                    zombie.setX(zombie.getX() + 100);
                }
                break;
        }
    }

    @Override
    public String getElement() {
        return element;
    }


}
