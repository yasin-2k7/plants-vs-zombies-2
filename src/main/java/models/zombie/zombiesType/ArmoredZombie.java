package models.zombie.zombiesType;

import models.zombie.Zombie;
import models.enums.Zombies;

public class ArmoredZombie extends Zombie {
    private int armorHealth;
    private boolean isMagnetic; // سطل و کلاه شوالیه بله، بلوک و مخروطی خیر

    public ArmoredZombie(int health, double speed, int damage, int armorHealth, boolean isMagnetic) {
        super(Zombies.ARMORED, health, speed, damage);
        this.armorHealth = armorHealth;
        this.isMagnetic = isMagnetic;
    }
    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        if (armorHealth > 0) {
            armorHealth -= amount;
            if (armorHealth < 0) armorHealth = 0;
        }else {
            super.takeDamage(amount, damageType);
        }
    }

    // متدی برای مگنت‌شروم
    public void stripArmor() {
        if (isMagnetic) this.armorHealth = 0;
    }

    public int getArmorHealth() { return armorHealth; }
    public boolean isMagnetic() { return isMagnetic; }
}
