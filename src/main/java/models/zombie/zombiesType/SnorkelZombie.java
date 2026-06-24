package models.zombie.zombiesType;

import models.enums.Zombies;
import models.zombie.Zombie;

public class SnorkelZombie extends Zombie {
    private boolean isSubmerged;

    public SnorkelZombie(int health, int speed, int damage) {
        super(Zombies.SNORKEL, health, speed, damage);
        this.isSubmerged = true;
    }
    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        if (isSubmerged && !"LOBBER".equals(damageType)) {
            return;
        }
        super.takeDamage(amount, damageType);
    }

    // این متد زمانی صدا زده می‌شود که زامبی به گیاه می‌رسد و باید برای خوردن از آب بیرون بیاید
    public void emergeToEat() {
        this.isSubmerged = false;
    }

    public void submerge() {
        this.isSubmerged = true;
    }

    public boolean isSubmerged() { return isSubmerged; }
}
