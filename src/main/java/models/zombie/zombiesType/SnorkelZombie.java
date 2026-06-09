package models.zombie.zombiesType;

import models.zombie.Zombie;

public class SnorkelZombie extends Zombie {
    private boolean isSubmerged;

    public SnorkelZombie(int health, int speed, int damage) {
        super("Snorkel Zombie", health, speed, damage);
        this.isSubmerged = true;
    }

    public void takeDamage(int damageAmount, boolean isLobberAttack) {
        if (isSubmerged && !isLobberAttack) return;
        this.health -= damageAmount;
        if(this.health <= 0) die();
    }

    // این متد زمانی صدا زده می‌شود که زامبی به گیاه می‌رسد و باید برای خوردن از آب بیرون بیاید
    public void emergeToEat() {
        this.isSubmerged = false;

    }
}
