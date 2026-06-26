package models.projectile.hitStrategies;

import models.enums.ProjectileType;
import models.projectile.Projectile;
import models.zombie.Zombie;

import java.util.List;

public class CombinedDamageStrategy implements HitStrategy{
    private int damage;
    private int neighborDamage = 0;
    private float radius = 0;
    private String element = "NORMAL";
    private int chillTime = 50;
    private float poisonDamageOnTick = 5;
    private ProjectileType projectileType;

    public float getPoisonDamageOnTick() {
        return poisonDamageOnTick;
    }

    public void setPoisonDamageOnTick(float poisonDamageOnTick) {
        this.poisonDamageOnTick = poisonDamageOnTick;
    }

    public void setChillTime(int chillTime) {
        this.chillTime = chillTime;
    }

    public int getChillTime() {
        return chillTime;
    }

    public CombinedDamageStrategy(int damage, ProjectileType projectileType) {
        this.damage = damage;
        this.projectileType = projectileType;
    }

    public void setElement(String element){
        this.element = element;
    }

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
        return new CombinedDamageStrategy(damage, neighborDamage, radius, projectileType);
    }

    public CombinedDamageStrategy changeProjectileType(ProjectileType type){
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, neighborDamage, radius, projectileType);
        combinedDamageStrategy.projectileType = type;
        return combinedDamageStrategy;
    }

    @Override
    public void applyDamage(Zombie target, List<Zombie> allZombies, Projectile projectile) {
        target.takeDamage(damage);
        applySpecialDamage(target, projectile);
        if (radius > 0) {
            for (Zombie zombie : allZombies){
                if (zombie != target){
                    if (projectile.distanceToZombie(zombie) <= radius){
                        zombie.takeDamage(neighborDamage);
                        applySpecialDamage(zombie, projectile);
                    }
                }
            }
        }
    }

    private void applySpecialDamage(Zombie target, Projectile projectile){
        switch (element){
            case "NORMAL":
                break;
            case "ICE":
                break;
            case "FIRE":
                break;
            case "STUN":
                break;
            case "POISON":
                break;
            case "MOVE":
                break;
        }
    }
}
