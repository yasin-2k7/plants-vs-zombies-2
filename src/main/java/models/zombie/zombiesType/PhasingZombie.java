package models.zombie.zombiesType;

import models.enums.Zombies;
import models.zombie.Zombie;

public class PhasingZombie extends Zombie {
    private boolean isPhaseChanged;
    private boolean isNewspaper; // true: newspaper, false: all-star
    private int shieldHealth; // جان روزنامه

    public PhasingZombie(int health, double speed, int damage, int shieldHealth, boolean isNewspaper) {
        super(Zombies.PHASING, health, speed, damage);
        this.shieldHealth = shieldHealth;
        this.isNewspaper = isNewspaper;
        this.isPhaseChanged = false;
        if (!isNewspaper) {
            // آل‌استار با سرعت بالا شروع می‌کند
            this.speed = (int)(speed * 2.5);
        }
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        if (!isPhaseChanged && shieldHealth > 0) {
            shieldHealth -= amount;
            if (shieldHealth <= 0) {
                triggerPhaseChange();
            }
        } else {
            super.takeDamage(amount, damageType);
        }
    }

    private void triggerPhaseChange() {
        this.isPhaseChanged = true;
        if (isNewspaper) {
            // نیوزپیپر عصبانی می‌شود
            this.speed = (int)(this.speed * 3);
            this.damage = (int)(this.damage * 2);
        } else {
            // آل‌استار کند می‌شود
            this.speed = (int)(this.speed * 0.3);
        }
    }
}
