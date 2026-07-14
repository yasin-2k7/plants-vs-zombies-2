package models;

public interface Damageable {
    void takeDamage(int damage, String damageType);
    float getX();
    float getY();
}
