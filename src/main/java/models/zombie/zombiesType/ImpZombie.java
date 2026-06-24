package models.zombie.zombiesType;

import models.enums.Zombies;
import models.plant.Plant;
import models.zombie.Zombie;

public class ImpZombie extends Zombie {
    private boolean isDragon;
    private boolean isThrown;

    public ImpZombie(int health, int speed, int damage, boolean isThrown) {
        super(Zombies.IMP, health, speed, damage);
        this.isDragon = isDragon;
        this.isThrown = false;
    }

    public void land(float targetX, float targetY) {
        this.x = targetX;
        this.y = targetY;
        this.isThrown = false;
    }

    public void bite(Plant plant) {
        if (plant != null) {
            plant.takeDamage(this.damage);
        }
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        // ایمپ اژدها در برابر آتش آسیب نمی‌بیند
        if (isDragon && "FIRE".equals(damageType)) {
            return;
        }
        super.takeDamage(amount, damageType);
    }

    public boolean isDragon() { return isDragon; }
    public boolean isThrown() { return isThrown; }
}
