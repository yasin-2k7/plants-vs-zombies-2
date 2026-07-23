package models.zombie.zombiesType;

import models.enums.Zombies;
import models.plant.Plant;
import models.zombie.Zombie;

public class ImpZombie extends Zombie {
    private boolean isDragon;
    private boolean isThrown; // true اگر توسط غول‌پیکر پرتاب شده باشد

    public ImpZombie(int health, double speed, int damage, boolean isDragon) {
        super(Zombies.IMP, health, speed, damage);
        this.isDragon = isDragon;
        this.isThrown = false;
    }

    public void throwImp(float targetX, float targetY) {
        this.x = targetX;
        this.y = targetY;
        this.isThrown = true;
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        System.out.println("🔍 ImpZombie.takeDamage: amount=" + amount + ", damageType='" + damageType + "'");
        if (isDead) return;
        if (isDragon && damageType != null && damageType.toUpperCase().contains("FIRE")) {
            System.out.println("🛡️ ImpDragon ignored " + amount + " fire damage.");
            return;
        }
        super.takeDamage(amount, damageType);
    }

    public boolean isDragon() { return isDragon; }
    public boolean isThrown() { return isThrown; }
}