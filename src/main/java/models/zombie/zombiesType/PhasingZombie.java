package models.zombie.zombiesType;

import models.zombie.Zombie;

public class PhasingZombie extends Zombie {
    private boolean isPhaseChanged = false;
    private int shieldHealth; // جان روزنامه

    public PhasingZombie(int health, int speed, int damage, int shieldHealth) {
        super("Phasing Zombie", health, speed, damage);
        this.shieldHealth = shieldHealth;
    }

    @Override
    public void takeDamage(int amount, String damageType) {
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
        this.speed = this.speed * 3; // افزایش سرعت برای نیوزپیپر
        // یا برای فوتبالیست: کاهش سرعت بعد از ضربه اول
    }
}
