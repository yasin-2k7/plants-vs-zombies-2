package models.zombie.zombiesType;

import models.zombie.Zombie;

public class ArmoredZombie extends Zombie {
    private int armorHealth;
    private boolean isMagnetic; // سطل و کلاه شوالیه بله، بلوک خیر

    public ArmoredZombie(int health, int speed, int damage, int armorHealth, boolean isMagnetic) {
        super("Armored Zombie", health, speed, damage);
        this.armorHealth = armorHealth;
        this.isMagnetic = isMagnetic;
    }

    public void takeDamage(int amount, boolean isLobberAttack) {
        if (armorHealth > 0) {
            armorHealth -= amount;
            if (armorHealth < 0) armorHealth = 0;
            //System.out.println("Armor health: " + armorHealth);
        } else {

        }
    }

    // متدی برای مگنت‌شروم
    public void stripArmor() {
        if (isMagnetic) this.armorHealth = 0;
    }
}
