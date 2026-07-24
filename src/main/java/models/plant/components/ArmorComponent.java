package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;
import models.zombie.Zombie;

public class ArmorComponent implements GameComponent {
    private int armorHp;
    private int initHp;

    public ArmorComponent(int armorHp) {
        this.armorHp = armorHp;
        initHp = armorHp;
    }

    @Override
    public void update(Plant owner) {
    }

    @Override
    public int onTakeDamage(Plant owner, int damageAmount, Zombie attacker) {
        if (armorHp <= 0) {
            return damageAmount;
        }

        if (damageAmount <= armorHp) {
            armorHp -= damageAmount;
            return 0;
        } else {
            int remainingDamage = damageAmount - armorHp;
            armorHp = 0;
            onDestroy(owner);
            return remainingDamage;
        }
    }

    @Override
    public void activatePlantFood(Plant owner) {
        armorHp = initHp;
    }

    protected void onDestroy(Plant owner) {

    }

    public void setArmorHp(int armorHp) {
        this.armorHp = armorHp;
    }
}
