package models;

import models.zombie.Zombie;

public interface Damageable {
    void takeDamage(int damage, String damageType);
    void takeDamage(int damage, Zombie zombie);
    float getX();
    float getY();
}
